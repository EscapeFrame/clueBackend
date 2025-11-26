package hello.cluebackend.domain.quizbattle.service;

import hello.cluebackend.application.agent.dto.response.AgentResponse;
import hello.cluebackend.application.quizbattle.dto.QuizGenerationRequest;
import hello.cluebackend.application.quizbattle.dto.QuizGenerationResponse;
import hello.cluebackend.domain.classroom.model.ClassRoom;
import hello.cluebackend.domain.quizbattle.model.*;
import hello.cluebackend.domain.user.model.UserEntity;
import hello.cluebackend.infrastructure.client.quiz.QuizClient;
import hello.cluebackend.infrastructure.persistence.classroom.ClassRoomJpaRepository;
import hello.cluebackend.infrastructure.persistence.quizroom.QuizRoomJpaRepository;
import hello.cluebackend.infrastructure.persistence.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class QuizBattleService {
  private final QuizRoomJpaRepository quizRoomRepository;
  private final UserJpaRepository userRepository;
  private final ClassRoomJpaRepository classRoomRepository;
  private final QuizRoomRedisService redisService;
  private final QuizClient quizClient;

  private static final String ROOM_CODE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static final int ROOM_CODE_LENGTH = 6;
  private static final Random random = new Random();

  public QuizRoom createRoom(
          UUID hostId, Integer maxParticipants, Integer questionCount,
          Integer timePerQuestion, UUID classRoomId, UUID documentId
  ) {
    UserEntity host = userRepository.findById(hostId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + hostId));
    ClassRoom classRoom = null;

    if (classRoomId != null) {
      classRoom = classRoomRepository.findById(classRoomId)
              .orElseThrow(() -> new IllegalArgumentException("Classroom not found: " + classRoomId));
    }

    String roomCode = generateUniqueRoomCode();

    QuizRoom quizRoom = QuizRoom.builder()
            .roomCode(roomCode)
            .host(host)
            .classRoom(classRoom)
            .status(QuizRoomStatus.WAITING)
            .maxParticipants(maxParticipants != null ? maxParticipants : 50)
            .questionCount(questionCount != null ? questionCount : 10)
            .timePerQuestion(timePerQuestion != null ? timePerQuestion : 30)
            .build();

    QuizRoom savedRoom = quizRoomRepository.save(quizRoom);

    int finalQuestionCount = questionCount != null ? questionCount : 10;
    List<QuizQuestion> questions = generateQuestions(finalQuestionCount, documentId);
    redisService.storeQuestions(roomCode, questions);

    log.info("with code: {} and {} questions", roomCode, questions.size());

    return savedRoom;
  }

  public QuizParticipant joinRoom(String roomCode, UUID userId, String sessionId) {
    QuizRoom room = quizRoomRepository.findByRoomCode(roomCode)
            .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomCode));

    if (!room.canJoin()) {
      throw new IllegalStateException("Cannot join room in current status: " + room.getStatus());
    }

    int currentParticipants = redisService.getParticipantCount(roomCode);
    if (currentParticipants >= room.getMaxParticipants()) {
      throw new IllegalStateException("Room is full");
    }

    UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

    QuizParticipant participant = QuizParticipant.builder()
            .userId(userId)
            .username(user.getUsername())
            .sessionId(sessionId)
            .score(0)
            .correctAnswers(0)
            .isReady(false)
            .joinedAt(System.currentTimeMillis())
            .build();

    redisService.addParticipant(roomCode, participant);
    log.info("User {} joined room {}", user.getUsername(), roomCode);

    return participant;
  }

    public List<QuizQuestion> startQuiz(String roomCode) {
        QuizRoom room = quizRoomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomCode));

        if (room.getStatus() != QuizRoomStatus.WAITING) {
            throw new IllegalStateException("Quiz already started or finished");
        }

        List<QuizQuestion> questions = redisService.getAllQuestions(roomCode);
        if (questions.isEmpty()) {
            throw new IllegalStateException("No questions found for room: " + roomCode);
        }

        redisService.setCurrentQuestion(roomCode, 1);

        room.start();
        quizRoomRepository.save(room);

        log.info("Started quiz for room {} with {} questions", roomCode, questions.size());

        return questions;
    }

    private List<QuizQuestion> generateQuestions(int count, UUID documentId) {
        try {
            QuizGenerationRequest request = QuizGenerationRequest.builder()
                    .questionCount(count)
                    .difficulty("Medium")
                    .language("ko")
                    .documentId(documentId)
                    .build();

            AgentResponse<QuizGenerationResponse> response = quizClient.generateQuiz(request);

            if (response.getData() != null && response.getData().getQuestions() != null) {
                return response.getData().getQuestions();
            } else {
                log.error("Failed to generate questions from FastAPI: {}", response.getMessage());
                throw new RuntimeException("Failed to generate questions");
            }
        } catch (Exception e) {
            log.error("Error calling quiz generation service", e);
            throw new RuntimeException("Failed to generate questions: " + e.getMessage());
        }
    }

    public QuizAnswer submitAnswer(
            String roomCode, UUID userId, int questionNumber,
            int answerIndex, long submittedAt, int timeSpent) {
        if (redisService.hasAnswered(roomCode, questionNumber, userId)) {
            throw new IllegalStateException("Already answered this question");
        }

        QuizQuestion question = redisService.getQuestion(roomCode, questionNumber);
        if (question == null) {
            throw new IllegalArgumentException("Question not found");
        }

        boolean isCorrect = question.isCorrect(answerIndex);

        int basePoints = 100;
        int maxTimeBonus = 50;
        int timeBonus = 0;

        if (isCorrect && timeSpent < question.getTimeLimit() * 1000) {
            double timeRatio = (double) timeSpent / (question.getTimeLimit() * 1000);
            timeBonus = (int) (maxTimeBonus * (1 - timeRatio));
        }

        QuizAnswer answer = QuizAnswer.builder()
                .userId(userId)
                .roomCode(roomCode)
                .questionNumber(questionNumber)
                .answerIndex(answerIndex)
                .submittedAt(submittedAt)
                .timeSpent(timeSpent)
                .build();

        answer.calculatePoints(isCorrect, basePoints, timeBonus);

        redisService.storeAnswer(roomCode, answer);

        redisService.updateParticipantScore(roomCode, userId, answer.getPoints(), isCorrect);

        log.info("User {} submitted answer for question {} in room {}, correct: {}, points: {}",
                userId, questionNumber, roomCode, isCorrect, answer.getPoints());

        return answer;
    }

    public List<QuizRanking> getRankings(String roomCode) {
        QuizRoom room = quizRoomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomCode));

        return redisService.calculateRankings(roomCode, room.getQuestionCount());
    }

    public void finishQuiz(String roomCode) {
        QuizRoom room = quizRoomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomCode));

        room.finish();
        quizRoomRepository.save(room);

        log.info("Finished quiz for room {}", roomCode);
    }

    public void cancelRoom(String roomCode) {
        QuizRoom room = quizRoomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomCode));

        room.cancel();
        quizRoomRepository.save(room);

        redisService.clearRoomData(roomCode);

        log.info("Cancelled room {}", roomCode);
    }

    public void leaveRoom(String roomCode, UUID userId) {
        redisService.removeParticipant(roomCode, userId);
        log.info("User {} left room {}", userId, roomCode);
    }

    public void leaveRoomBySessionId(String sessionId) {
        String roomCode = redisService.getRoomCodeBySessionId(sessionId);
        if (roomCode != null) {
            UUID userId = redisService.getUserIdBySessionId(roomCode, sessionId);
            if (userId != null) {
                redisService.removeParticipant(roomCode, userId);
                log.info("User {} left room {} (disconnected session: {})", userId, roomCode, sessionId);
            }
        }
    }

    public QuizRoom getRoom(String roomCode) {
        return quizRoomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomCode));
    }

    public List<QuizParticipant> getParticipants(String roomCode) {
        return redisService.getAllParticipants(roomCode);
    }

    public QuizQuestion getCurrentQuestion(String roomCode) {
        Integer questionNumber = redisService.getCurrentQuestionNumber(roomCode);
        if (questionNumber == null) {
            return null;
        }
        return redisService.getQuestion(roomCode, questionNumber);
    }

    public Integer getCurrentQuestionNumber(String roomCode) {
        return redisService.getCurrentQuestionNumber(roomCode);
    }

    public void nextQuestion(String roomCode) {
        Integer currentQuestion = redisService.getCurrentQuestionNumber(roomCode);
        if (currentQuestion != null) {
            redisService.setCurrentQuestion(roomCode, currentQuestion + 1);
        }
    }

    private String generateUniqueRoomCode() {
        String code;
        do {
            code = generateRandomCode();
        } while (quizRoomRepository.existsByRoomCode(code));
        return code;
    }

    private String generateRandomCode() {
        StringBuilder code = new StringBuilder(ROOM_CODE_LENGTH);
        for (int i = 0; i < ROOM_CODE_LENGTH; i++) {
            code.append(ROOM_CODE_CHARS.charAt(random.nextInt(ROOM_CODE_CHARS.length())));
        }
        return code.toString();
    }
}

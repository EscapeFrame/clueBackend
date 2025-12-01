package hello.cluebackend.domain.quizbattle.service;

import hello.cluebackend.application.agent.dto.response.AgentResponse;
import hello.cluebackend.application.quizbattle.dto.QuizGenerationRequest;
import hello.cluebackend.application.quizbattle.dto.QuizGenerationResponse;
import hello.cluebackend.domain.classroom.model.ClassRoom;
import hello.cluebackend.domain.document.model.Document;
import hello.cluebackend.domain.quizbattle.model.*;
import hello.cluebackend.domain.user.model.UserEntity;
import hello.cluebackend.infrastructure.client.quiz.QuizClient;
import hello.cluebackend.infrastructure.persistence.classroom.ClassRoomJpaRepository;
import hello.cluebackend.infrastructure.persistence.document.DocumentJpaRepository;
import hello.cluebackend.infrastructure.persistence.quizroom.QuizRoomJpaRepository;
import hello.cluebackend.infrastructure.persistence.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  private final DocumentJpaRepository documentRepository;
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

    Document document = documentRepository.findById(documentId)
            .orElseThrow(() -> new IllegalArgumentException("Document not found: " + documentId));


    String roomCode = generateUniqueRoomCode();

    int finalTimePerQuestion = timePerQuestion != null ? timePerQuestion : 30;

    QuizRoom quizRoom = QuizRoom.builder()
            .title(document.getTitle())
            .roomCode(roomCode)
            .host(host)
            .classRoom(classRoom)
            .status(QuizRoomStatus.WAITING)
            .maxParticipants(maxParticipants != null ? maxParticipants : 50)
            .questionCount(questionCount != null ? questionCount : 10)
            .timePerQuestion(finalTimePerQuestion)
            .build();

    QuizRoom savedRoom = quizRoomRepository.save(quizRoom);

    int finalQuestionCount = questionCount != null ? questionCount : 10;
    List<QuizQuestion> questions = generateQuestions(finalQuestionCount, documentId, finalTimePerQuestion);
    redisService.storeQuestions(roomCode, questions);

    // 실제 생성된 문제 개수가 요청한 개수와 다를 경우 업데이트
    if (questions.size() != finalQuestionCount) {
        savedRoom.updateQuestionCount(questions.size());
        quizRoomRepository.save(savedRoom);
        log.info("Updated question count from {} to {} for room {}",
            finalQuestionCount, questions.size(), roomCode);
    }

    // 호스트 정보를 레디스에 저장 (권한 확인용, 참가자 목록과 별도)
    redisService.setHost(roomCode, hostId, null);
    log.info("Created room {} with {} questions, host {} set",
        roomCode, questions.size(), host.getUsername());

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

    private List<QuizQuestion> generateQuestions(int count, UUID documentId, int timePerQuestion) {
        try {
            QuizGenerationRequest request = QuizGenerationRequest.builder()
                    .questionCount(count)
                    .difficulty("Medium")
                    .language("ko")
                    .documentId(documentId)
                    .build();

            log.info("Requesting {} quiz questions for document {}", count, documentId);
            AgentResponse<QuizGenerationResponse> response = quizClient.generateQuiz(request);

            if (response.getData() != null && response.getData().getQuestions() != null) {
                List<QuizQuestion> questions = response.getData().getQuestions();

                // 반환된 문제 개수 확인
                if (questions.isEmpty()) {
                    log.error("No questions generated from FastAPI");
                    throw new RuntimeException("No questions were generated");
                }

                // 요청한 개수보다 많으면 자르기
                if (questions.size() > count) {
                    log.warn("Generated {} questions but only {} were requested. Trimming...",
                        questions.size(), count);
                    questions = questions.subList(0, count);
                }

                // 요청한 개수보다 적으면 경고
                if (questions.size() < count) {
                    log.warn("Only {} questions generated, but {} were requested",
                        questions.size(), count);
                }

                // Manually set question numbers and time limits
                for (int i = 0; i < questions.size(); i++) {
                    QuizQuestion question = questions.get(i);
                    question.setQuestionNumber(i + 1);
                    question.setTimeLimit(timePerQuestion);
                    log.debug("Question {} generated: text={}, options={}, correctAnswer={}",
                        i + 1, question.getQuestionText(),
                        question.getOptions() != null ? question.getOptions().size() : "null",
                        question.getCorrectAnswer());
                }

                log.info("Successfully generated {} questions (requested: {})", questions.size(), count);
                return questions;
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
        QuizRoom room = quizRoomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomCode));

        if (room.getStatus() != QuizRoomStatus.IN_PROGRESS) {
            throw new IllegalStateException("Quiz is not in progress");
        }

        Integer currentQuestionNum = redisService.getCurrentQuestionNumber(roomCode);
        if (currentQuestionNum == null || currentQuestionNum != questionNumber) {
            throw new IllegalStateException("Not the current question");
        }

        String questionStatus = redisService.getQuestionStatus(roomCode, questionNumber);
        if ("REVEALED".equals(questionStatus)) {
            throw new IllegalStateException("Answer already revealed, cannot submit");
        }

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

    public DisconnectResult handleDisconnect(String sessionId) {
        String roomCode = redisService.getRoomCodeBySessionId(sessionId);
        if (roomCode == null) {
            log.debug("No room found for disconnected session: {}", sessionId);
            return null;
        }

        UUID userId = redisService.getUserIdBySessionId(roomCode, sessionId);
        if (userId == null) {
            log.debug("No user found for disconnected session: {}", sessionId);
            return null;
        }

        QuizRoom room = quizRoomRepository.findByRoomCode(roomCode).orElse(null);
        if (room == null) {
            log.debug("Room {} not found in database for disconnected session: {}", roomCode, sessionId);
            return null;
        }

        boolean wasHost = room.isHost(userId);

        redisService.removeParticipant(roomCode, userId);
        redisService.removeSessionMapping(sessionId);

        log.info("User {} disconnected from room {} (host: {})", userId, roomCode, wasHost);

        return DisconnectResult.builder()
                .roomCode(roomCode)
                .userId(userId)
                .wasHost(wasHost)
                .build();
    }

    @lombok.Builder
    @lombok.Getter
    public static class DisconnectResult {
        private String roomCode;
        private UUID userId;
        private boolean wasHost;
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

    public void setQuestionActive(String roomCode, int questionNumber) {
        redisService.setQuestionStatus(roomCode, questionNumber, "ACTIVE");
        log.info("Set question {} to ACTIVE in room {}", questionNumber, roomCode);
    }

    public Map<String, Object> revealAnswer(String roomCode, int questionNumber) {
        QuizQuestion question = redisService.getQuestion(roomCode, questionNumber);
        if (question == null) {
            throw new IllegalArgumentException("Question not found: " + questionNumber);
        }

        String currentStatus = redisService.getQuestionStatus(roomCode, questionNumber);
        if ("REVEALED".equals(currentStatus)) {
            throw new IllegalStateException("Answer already revealed for question " + questionNumber);
        }

        redisService.setQuestionStatus(roomCode, questionNumber, "REVEALED");

        Map<Integer, Integer> statistics = redisService.getAnswerStatistics(roomCode, questionNumber);
        int totalAnswers = statistics.values().stream().mapToInt(Integer::intValue).sum();

        Map<String, Object> result = new HashMap<>();
        result.put("correctAnswer", question.getCorrectAnswer());
        result.put("explanation", question.getExplanation());
        result.put("statistics", statistics);
        result.put("totalAnswers", totalAnswers);

        log.info("Revealed answer for question {} in room {}", questionNumber, roomCode);

        return result;
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

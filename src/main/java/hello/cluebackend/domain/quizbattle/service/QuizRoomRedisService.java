package hello.cluebackend.domain.quizbattle.service;

import hello.cluebackend.domain.quizbattle.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizRoomRedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String PARTICIPANT_KEY_PREFIX = "quiz:room:";
    private static final String PARTICIPANT_KEY_SUFFIX = ":participants";
    private static final String QUESTION_KEY_PREFIX = "quiz:room:";
    private static final String QUESTION_KEY_SUFFIX = ":questions";
    private static final String ANSWER_KEY_PREFIX = "quiz:room:";
    private static final String ANSWER_KEY_SUFFIX = ":answers";
    private static final String CURRENT_QUESTION_KEY_PREFIX = "quiz:room:";
    private static final String CURRENT_QUESTION_KEY_SUFFIX = ":current";
    private static final String QUESTION_STATUS_KEY_PREFIX = "quiz:room:";
    private static final String QUESTION_STATUS_KEY_SUFFIX = ":status";
    private static final String SESSION_MAPPING_PREFIX = "quiz:session:";
    private static final long DEFAULT_EXPIRATION_HOURS = 24;

    public void addParticipant(String roomCode, QuizParticipant participant) {
        String key = getParticipantKey(roomCode);
        redisTemplate.opsForHash().put(key, participant.getUserId().toString(), participant);
        redisTemplate.expire(key, DEFAULT_EXPIRATION_HOURS, TimeUnit.HOURS);

        // Store session to room mapping
        if (participant.getSessionId() != null) {
            addSessionMapping(participant.getSessionId(), roomCode);
        }

        log.info("Added participant {} to room {}", participant.getUsername(), roomCode);
    }

    public void removeParticipant(String roomCode, UUID userId) {
        String key = getParticipantKey(roomCode);
        QuizParticipant participant = getParticipant(roomCode, userId);

        redisTemplate.opsForHash().delete(key, userId.toString());

        // Remove session to room mapping
        if (participant != null && participant.getSessionId() != null) {
            removeSessionMapping(participant.getSessionId());
        }

        log.info("Removed participant {} from room {}", userId, roomCode);
    }

    public List<QuizParticipant> getAllParticipants(String roomCode) {
        String key = getParticipantKey(roomCode);
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        return entries.values().stream()
                .map(obj -> (QuizParticipant) obj)
                .collect(Collectors.toList());
    }

    public QuizParticipant getParticipant(String roomCode, UUID userId) {
        String key = getParticipantKey(roomCode);
        return (QuizParticipant) redisTemplate.opsForHash().get(key, userId.toString());
    }

    public void updateParticipantScore(String roomCode, UUID userId, int points, boolean incrementCorrect) {
        QuizParticipant participant = getParticipant(roomCode, userId);
        if (participant != null) {
            participant.addScore(points);
            if (incrementCorrect) {
                participant.incrementCorrectAnswers();
            }
            addParticipant(roomCode, participant);
        }
    }

    public int getParticipantCount(String roomCode) {
        String key = getParticipantKey(roomCode);
        Long size = redisTemplate.opsForHash().size(key);
        return size != null ? size.intValue() : 0;
    }

    public void clearParticipants(String roomCode) {
        String key = getParticipantKey(roomCode);
        redisTemplate.delete(key);
        log.info("Cleared all participants from room {}", roomCode);
    }

    public void storeQuestions(String roomCode, List<QuizQuestion> questions) {
        String key = getQuestionKey(roomCode);
        for (QuizQuestion question : questions) {
            redisTemplate.opsForList().rightPush(key, question);
        }
        redisTemplate.expire(key, DEFAULT_EXPIRATION_HOURS, TimeUnit.HOURS);
        log.info("Stored {} questions for room {}", questions.size(), roomCode);
    }

    public QuizQuestion getQuestion(String roomCode, int questionNumber) {
        String key = getQuestionKey(roomCode);
        Object question = redisTemplate.opsForList().index(key, questionNumber - 1);
        if (question != null) {
            QuizQuestion q = (QuizQuestion) question;
            log.debug("Retrieved question {} from Redis: text={}, options={}, correctAnswer={}",
                questionNumber, q.getQuestionText(),
                q.getOptions() != null ? q.getOptions().size() : "null",
                q.getCorrectAnswer());
            return q;
        }
        return null;
    }

    public List<QuizQuestion> getAllQuestions(String roomCode) {
        String key = getQuestionKey(roomCode);
        List<Object> questions = redisTemplate.opsForList().range(key, 0, -1);
        return questions != null ? questions.stream()
                .map(obj -> (QuizQuestion) obj)
                .collect(Collectors.toList()) : Collections.emptyList();
    }

    public void setCurrentQuestion(String roomCode, int questionNumber) {
        String key = getCurrentQuestionKey(roomCode);
        redisTemplate.opsForValue().set(key, questionNumber, DEFAULT_EXPIRATION_HOURS, TimeUnit.HOURS);
        log.info("Set current question to {} for room {}", questionNumber, roomCode);
    }

    public Integer getCurrentQuestionNumber(String roomCode) {
        String key = getCurrentQuestionKey(roomCode);
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? (Integer) value : null;
    }

    public void clearQuestions(String roomCode) {
        String key = getQuestionKey(roomCode);
        redisTemplate.delete(key);
        log.info("Cleared all questions from room {}", roomCode);
    }

    public void storeAnswer(String roomCode, QuizAnswer answer) {
        String key = getAnswerKey(roomCode, answer.getQuestionNumber());
        redisTemplate.opsForHash().put(key, answer.getUserId().toString(), answer);
        redisTemplate.expire(key, DEFAULT_EXPIRATION_HOURS, TimeUnit.HOURS);
        log.info("Stored answer from user {} for question {} in room {}",
                answer.getUserId(), answer.getQuestionNumber(), roomCode);
    }

    public List<QuizAnswer> getAnswersForQuestion(String roomCode, int questionNumber) {
        String key = getAnswerKey(roomCode, questionNumber);
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        return entries.values().stream()
                .map(obj -> (QuizAnswer) obj)
                .collect(Collectors.toList());
    }

    public boolean hasAnswered(String roomCode, int questionNumber, UUID userId) {
        String key = getAnswerKey(roomCode, questionNumber);
        return Boolean.TRUE.equals(redisTemplate.opsForHash().hasKey(key, userId.toString()));
    }

    public QuizAnswer getUserAnswer(String roomCode, int questionNumber, UUID userId) {
        String key = getAnswerKey(roomCode, questionNumber);
        return (QuizAnswer) redisTemplate.opsForHash().get(key, userId.toString());
    }

    public void clearAnswers(String roomCode) {
        log.info("Answer clearing scheduled for room {} after expiration", roomCode);
    }

    public List<QuizRanking> calculateRankings(String roomCode, int totalQuestions) {
        List<QuizParticipant> participants = getAllParticipants(roomCode);

        List<QuizRanking> rankings = participants.stream()
                .map(p -> QuizRanking.fromParticipant(p, totalQuestions))
                .sorted((r1, r2) -> {
                    // Sort by score descending, then by accuracy
                    int scoreCompare = r2.getTotalScore().compareTo(r1.getTotalScore());
                    if (scoreCompare != 0) return scoreCompare;
                    return r2.getAccuracy().compareTo(r1.getAccuracy());
                })
                .collect(Collectors.toList());

        // Assign ranks
        for (int i = 0; i < rankings.size(); i++) {
            rankings.get(i).setRank(i + 1);
        }

        return rankings;
    }

    public void clearRoomData(String roomCode) {
        clearParticipants(roomCode);
        clearQuestions(roomCode);
        String currentQuestionKey = getCurrentQuestionKey(roomCode);
        redisTemplate.delete(currentQuestionKey);
        log.info("Cleared all data for room {}", roomCode);
    }

    private String getParticipantKey(String roomCode) {
        return PARTICIPANT_KEY_PREFIX + roomCode + PARTICIPANT_KEY_SUFFIX;
    }

    private String getQuestionKey(String roomCode) {
        return QUESTION_KEY_PREFIX + roomCode + QUESTION_KEY_SUFFIX;
    }

    private String getAnswerKey(String roomCode, int questionNumber) {
        return ANSWER_KEY_PREFIX + roomCode + ANSWER_KEY_SUFFIX + ":" + questionNumber;
    }

    private String getCurrentQuestionKey(String roomCode) {
        return CURRENT_QUESTION_KEY_PREFIX + roomCode + CURRENT_QUESTION_KEY_SUFFIX;
    }

    private String getSessionMappingKey(String sessionId) {
        return SESSION_MAPPING_PREFIX + sessionId;
    }

    public void addSessionMapping(String sessionId, String roomCode) {
        String key = getSessionMappingKey(sessionId);
        redisTemplate.opsForValue().set(key, roomCode, DEFAULT_EXPIRATION_HOURS, TimeUnit.HOURS);
        log.info("Mapped session {} to room {}", sessionId, roomCode);
    }

    public String getRoomCodeBySessionId(String sessionId) {
        String key = getSessionMappingKey(sessionId);
        Object roomCode = redisTemplate.opsForValue().get(key);
        return roomCode != null ? (String) roomCode : null;
    }

    public void removeSessionMapping(String sessionId) {
        String key = getSessionMappingKey(sessionId);
        redisTemplate.delete(key);
        log.info("Removed session mapping for {}", sessionId);
    }

    public UUID getUserIdBySessionId(String roomCode, String sessionId) {
        List<QuizParticipant> participants = getAllParticipants(roomCode);
        return participants.stream()
                .filter(p -> sessionId.equals(p.getSessionId()))
                .map(QuizParticipant::getUserId)
                .findFirst()
                .orElse(null);
    }

    public void setQuestionStatus(String roomCode, int questionNumber, String status) {
        String key = getQuestionStatusKey(roomCode, questionNumber);
        redisTemplate.opsForValue().set(key, status, DEFAULT_EXPIRATION_HOURS, TimeUnit.HOURS);
        log.info("Set question {} status to {} for room {}", questionNumber, status, roomCode);
    }

    public String getQuestionStatus(String roomCode, int questionNumber) {
        String key = getQuestionStatusKey(roomCode, questionNumber);
        Object status = redisTemplate.opsForValue().get(key);
        return status != null ? (String) status : "ACTIVE";
    }

    public Map<Integer, Integer> getAnswerStatistics(String roomCode, int questionNumber) {
        List<QuizAnswer> answers = getAnswersForQuestion(roomCode, questionNumber);
        Map<Integer, Integer> statistics = new HashMap<>();

        for (QuizAnswer answer : answers) {
            int answerIndex = answer.getAnswerIndex();
            statistics.put(answerIndex, statistics.getOrDefault(answerIndex, 0) + 1);
        }

        return statistics;
    }

    private String getQuestionStatusKey(String roomCode, int questionNumber) {
        return QUESTION_STATUS_KEY_PREFIX + roomCode + QUESTION_STATUS_KEY_SUFFIX + ":" + questionNumber;
    }
}

package hello.cluebackend.presentation.websocket.quizbattle;

import hello.cluebackend.common.utils.JWTUtil;
import hello.cluebackend.domain.quizbattle.model.*;
import hello.cluebackend.domain.quizbattle.service.QuizBattleService;
import hello.cluebackend.domain.quizbattle.service.QuizTimerService;
import hello.cluebackend.presentation.websocket.quizbattle.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class QuizBattleWebSocketController {
    private final QuizBattleService quizBattleService;
    private final QuizTimerService quizTimerService;
    private final SimpMessagingTemplate messagingTemplate;
    private final JWTUtil jwtUtil;

    private UUID getUserIdFromHeader(SimpMessageHeaderAccessor headerAccessor) {
        try {
            // First, try to get from session attributes (set during STOMP CONNECT)
            Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
            if (sessionAttributes != null && sessionAttributes.containsKey("userId")) {
                Object userId = sessionAttributes.get("userId");
                if (userId instanceof UUID) {
                    log.debug("Extracted userId from session attributes: {}", userId);
                    return (UUID) userId;
                }
            }

            // Fallback: Try to get from message headers
            Map<String, Object> nativeHeaders = (Map<String, Object>) headerAccessor.getHeader("nativeHeaders");

            if (nativeHeaders != null && nativeHeaders.containsKey("Authorization")) {
                List<String> authHeaders = (List<String>) nativeHeaders.get("Authorization");
                if (authHeaders != null && !authHeaders.isEmpty()) {
                    String token = authHeaders.get(0);
                    // Remove "Bearer " prefix if present
                    if (token.startsWith("Bearer ")) {
                        token = token.substring(7);
                    }
                    log.debug("Extracted token from nativeHeaders: {}", token.substring(0, Math.min(20, token.length())) + "...");
                    UUID userId = jwtUtil.getUserId(token);
                    // Store in session for future requests
                    if (sessionAttributes != null) {
                        sessionAttributes.put("userId", userId);
                    }
                    return userId;
                }
            }

            log.error("Authorization not found. Session attrs: {}, Native headers: {}",
                    sessionAttributes != null ? sessionAttributes.keySet() : "null",
                    nativeHeaders != null ? nativeHeaders.keySet() : "null");
        } catch (Exception e) {
            log.error("Error extracting userId from header", e);
        }
        throw new IllegalArgumentException("Authorization token not found in message headers");
    }

    @MessageMapping("/quiz/create")
    @SendTo("/topic/quiz/rooms")
    public RoomCreatedMessage createRoom(
            @Payload CreateRoomRequest request,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        try {
            UUID userId = getUserIdFromHeader(headerAccessor);
            QuizRoom room = quizBattleService.createRoom(
                    userId,
                    request.getMaxParticipants(),
                    request.getQuestionCount(),
                    request.getTimePerQuestion(),
                    request.getClassRoomId(),
                    request.getDocumentId()
            );

            log.info("Room created: {} by user {}", room.getRoomCode(), userId);

            return RoomCreatedMessage.builder()
                    .roomCode(room.getRoomCode())
                    .hostId(room.getHost().getUserId())
                    .maxParticipants(room.getMaxParticipants())
                    .questionCount(room.getQuestionCount())
                    .timePerQuestion(room.getTimePerQuestion())
                    .status("success")
                    .message("Room created successfully")
                    .build();

        } catch (Exception e) {
            log.error("Error creating room", e);
            return RoomCreatedMessage.builder()
                    .status("error")
                    .message(e.getMessage())
                    .build();
        }
    }

    @MessageMapping("/quiz/join/{roomCode}")
    public void joinRoom(
            @DestinationVariable String roomCode,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        try {
            UUID userId = getUserIdFromHeader(headerAccessor);
            String sessionId = headerAccessor.getSessionId();

            QuizParticipant participant = quizBattleService.joinRoom(roomCode, userId, sessionId);
            List<QuizParticipant> allParticipants = quizBattleService.getParticipants(roomCode);

            ParticipantJoinedMessage message = ParticipantJoinedMessage.builder()
                    .participant(participant)
                    .totalParticipants(allParticipants.size())
                    .allParticipants(allParticipants)
                    .status("success")
                    .message(participant.getUsername() + " joined the room")
                    .build();

            messagingTemplate.convertAndSend("/topic/quiz/" + roomCode + "/participants", message);

            log.info("User {} joined room {}", participant.getUsername(), roomCode);

        } catch (Exception e) {
            log.error("Error joining room", e);
            ErrorMessage error = ErrorMessage.builder()
                    .status("error")
                    .message(e.getMessage())
                    .build();
            messagingTemplate.convertAndSendToUser(
                    headerAccessor.getSessionId(),
                    "/queue/errors",
                    error
            );
        }
    }

    @MessageMapping("/quiz/start/{roomCode}")
    public void startQuiz(
            @DestinationVariable String roomCode,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        try {
            UUID userId = getUserIdFromHeader(headerAccessor);
            QuizRoom room = quizBattleService.getRoom(roomCode);

            if (!room.isHost(userId)) {
                throw new IllegalStateException("Only the host can start the quiz");
            }

            List<QuizQuestion> questions = quizBattleService.startQuiz(roomCode);
            QuizQuestion firstQuestion = questions.get(0);
            sendQuestionToRoom(roomCode, firstQuestion);

            log.info("Quiz started in room {}", roomCode);

        } catch (Exception e) {
            log.error("Error starting quiz", e);
            ErrorMessage error = ErrorMessage.builder()
                    .status("error")
                    .message(e.getMessage())
                    .build();
            messagingTemplate.convertAndSend("/topic/quiz/" + roomCode + "/game", error);
        }
    }

    @MessageMapping("/quiz/answer/{roomCode}")
    public void submitAnswer(
            @DestinationVariable String roomCode,
            @Payload SubmitAnswerRequest request,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        try {
            UUID userId = getUserIdFromHeader(headerAccessor);
            QuizAnswer answer = quizBattleService.submitAnswer(
                    roomCode,
                    userId,
                    request.getQuestionNumber(),
                    request.getAnswerIndex(),
                    request.getSubmittedAt(),
                    request.getTimeSpent()
            );

            AnswerResultMessage result = AnswerResultMessage.builder()
                    .questionNumber(answer.getQuestionNumber())
                    .isCorrect(answer.getIsCorrect())
                    .points(answer.getPoints())
                    .status("success")
                    .build();

            messagingTemplate.convertAndSendToUser(
                    headerAccessor.getSessionId(),
                    "/queue/quiz/result",
                    result
            );

            log.info("User {} submitted answer for question {} in room {}",
                    userId, request.getQuestionNumber(), roomCode);

        } catch (Exception e) {
            log.error("Error submitting answer", e);
            ErrorMessage error = ErrorMessage.builder()
                    .status("error")
                    .message(e.getMessage())
                    .build();
            messagingTemplate.convertAndSendToUser(
                    headerAccessor.getSessionId(),
                    "/queue/errors",
                    error
            );
        }
    }

    @MessageMapping("/quiz/next/{roomCode}")
    public void nextQuestion(
            @DestinationVariable String roomCode,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        try {
            UUID userId = getUserIdFromHeader(headerAccessor);

            QuizRoom room = quizBattleService.getRoom(roomCode);

            if (!room.isHost(userId)) {
                throw new IllegalStateException("Only the host can move to next question");
            }

            Integer currentQuestionNum = quizBattleService.getCurrentQuestionNumber(roomCode);
            if (currentQuestionNum != null) {
                quizTimerService.cancelQuestionTimer(roomCode, currentQuestionNum);
            }

            moveToNextQuestion(roomCode);

        } catch (Exception e) {
            log.error("Error moving to next question", e);
            ErrorMessage error = ErrorMessage.builder()
                    .status("error")
                    .message(e.getMessage())
                    .build();
            messagingTemplate.convertAndSend("/topic/quiz/" + roomCode + "/game", error);
        }
    }

    @MessageMapping("/quiz/rankings/{roomCode}")
    public void getRankings(
            @DestinationVariable String roomCode
    ) {
        try {
            List<QuizRanking> rankings = quizBattleService.getRankings(roomCode);

            RankingMessage message = RankingMessage.builder()
                    .rankings(rankings)
                    .totalParticipants(rankings.size())
                    .status("success")
                    .build();

            messagingTemplate.convertAndSend("/topic/quiz/" + roomCode + "/rankings", message);

            log.info("Rankings requested for room {}", roomCode);

        } catch (Exception e) {
            log.error("Error getting rankings", e);
            ErrorMessage error = ErrorMessage.builder()
                    .status("error")
                    .message(e.getMessage())
                    .build();
            messagingTemplate.convertAndSend("/topic/quiz/" + roomCode + "/rankings", error);
        }
    }

    @MessageMapping("/quiz/leave/{roomCode}")
    public void leaveRoom(
            @DestinationVariable String roomCode,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        try {
            UUID userId = getUserIdFromHeader(headerAccessor);
            quizBattleService.leaveRoom(roomCode, userId);

            List<QuizParticipant> remainingParticipants = quizBattleService.getParticipants(roomCode);

            ParticipantLeftMessage message = ParticipantLeftMessage.builder()
                    .userId(userId)
                    .totalParticipants(remainingParticipants.size())
                    .allParticipants(remainingParticipants)
                    .status("success")
                    .build();

            messagingTemplate.convertAndSend("/topic/quiz/" + roomCode + "/participants", message);

            log.info("User {} left room {}", userId, roomCode);

        } catch (Exception e) {
            log.error("Error leaving room", e);
        }
    }

    @MessageMapping("/quiz/cancel/{roomCode}")
    public void cancelRoom(
            @DestinationVariable String roomCode,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        try {
            UUID userId = getUserIdFromHeader(headerAccessor);
            QuizRoom room = quizBattleService.getRoom(roomCode);

            if (!room.isHost(userId)) {
                throw new IllegalStateException("Only the host can cancel the room");
            }

            quizTimerService.cancelAllTimersForRoom(roomCode);
            quizBattleService.cancelRoom(roomCode);

            RoomCancelledMessage message = RoomCancelledMessage.builder()
                    .roomCode(roomCode)
                    .reason("cancelled_by_host")
                    .status("cancelled")
                    .message("Room has been cancelled by the host")
                    .build();

            messagingTemplate.convertAndSend("/topic/quiz/" + roomCode + "/game", message);

            log.info("Room {} cancelled by host {}", roomCode, userId);

        } catch (Exception e) {
            log.error("Error cancelling room", e);
            ErrorMessage error = ErrorMessage.builder()
                    .status("error")
                    .message(e.getMessage())
                    .build();
            messagingTemplate.convertAndSend("/topic/quiz/" + roomCode + "/game", error);
        }
    }

    private void sendQuestionToRoom(String roomCode, QuizQuestion question) {
        QuizQuestionMessage questionMessage = QuizQuestionMessage.builder()
                .questionNumber(question.getQuestionNumber())
                .questionText(question.getQuestionText())
                .options(question.getOptions())
                .timeLimit(question.getTimeLimit())
                .difficulty(question.getDifficulty())
                .status("success")
                .build();

        messagingTemplate.convertAndSend("/topic/quiz/" + roomCode + "/game", questionMessage);

        quizTimerService.scheduleQuestionTimeout(
                roomCode,
                question.getQuestionNumber(),
                question.getTimeLimit(),
                () -> {
                    try {
                        moveToNextQuestion(roomCode);
                    } catch (Exception e) {
                        log.error("Error auto-moving to next question in room {}", roomCode, e);
                    }
                }
        );

        log.info("Question {} sent to room {} with {} second timer",
                question.getQuestionNumber(), roomCode, question.getTimeLimit());
    }

    private void moveToNextQuestion(String roomCode) {
        quizBattleService.nextQuestion(roomCode);
        QuizQuestion nextQuestion = quizBattleService.getCurrentQuestion(roomCode);

        if (nextQuestion != null) {
            sendQuestionToRoom(roomCode, nextQuestion);
            log.info("Moved to question {} in room {}", nextQuestion.getQuestionNumber(), roomCode);
        } else {
            finishQuiz(roomCode);
        }
    }

    private void finishQuiz(String roomCode) {
        quizTimerService.cancelAllTimersForRoom(roomCode);

        quizBattleService.finishQuiz(roomCode);
        List<QuizRanking> finalRankings = quizBattleService.getRankings(roomCode);

        QuizFinishedMessage finishMessage = QuizFinishedMessage.builder()
                .roomCode(roomCode)
                .finalRankings(finalRankings)
                .status("finished")
                .message("Quiz completed!")
                .build();

        messagingTemplate.convertAndSend("/topic/quiz/" + roomCode + "/game", finishMessage);

        log.info("Quiz finished in room {}", roomCode);
    }
}

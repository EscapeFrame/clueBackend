package hello.cluebackend.presentation.websocket.quizbattle;

import hello.cluebackend.domain.quizbattle.model.QuizParticipant;
import hello.cluebackend.domain.quizbattle.service.QuizBattleService;
import hello.cluebackend.domain.quizbattle.service.QuizRoomRedisService;
import hello.cluebackend.domain.quizbattle.service.QuizTimerService;
import hello.cluebackend.presentation.websocket.quizbattle.dto.ParticipantLeftMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
  private final QuizTimerService quizTimerService;
  private final QuizBattleService quizBattleService;
  private final QuizRoomRedisService redisService;
  private final SimpMessagingTemplate messagingTemplate;

  @EventListener
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = headerAccessor.getSessionId();

    log.info("WebSocket session disconnected: {}", sessionId);

    if (sessionId != null) {
      try {
        String roomCode = redisService.getRoomCodeBySessionId(sessionId);

        if (roomCode != null) {
          java.util.UUID userId = redisService.getUserIdBySessionId(roomCode, sessionId);

          if (userId != null) {
            quizBattleService.leaveRoomBySessionId(sessionId);

            List<QuizParticipant> remainingParticipants = quizBattleService.getParticipants(roomCode);

            ParticipantLeftMessage message = ParticipantLeftMessage.builder()
                    .userId(userId)
                    .totalParticipants(remainingParticipants.size())
                    .allParticipants(remainingParticipants)
                    .status("success")
                    .build();

            messagingTemplate.convertAndSend("/topic/quiz/" + roomCode + "/participants", message);

            log.info("User {} automatically left room {} due to disconnection", userId, roomCode);
          }
        }
      } catch (Exception e) {
        log.error("Error handling WebSocket disconnection for session {}", sessionId, e);
      }
    }
  }
}

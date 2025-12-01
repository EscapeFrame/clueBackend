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
    System.out.println("###################### FUCK DISCONNECT #################");

    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = headerAccessor.getSessionId();

    log.info("WebSocket session disconnected: {}", sessionId);

    if (sessionId != null) {
      try {
        String roomCode = redisService.getRoomCodeBySessionId(sessionId);
        System.out.println("####### room code: " + roomCode);
        if (roomCode != null) {
          // 먼저 호스트인지 확인
          String hostSessionId = redisService.getHostSessionId(roomCode);
          boolean isHost = sessionId.equals(hostSessionId);

          if (isHost) {
            // 호스트가 연결 끊김
            java.util.UUID hostId = redisService.getHostId(roomCode);
            System.out.println("####### HOST disconnected, host id: " + hostId);

            // 타이머 취소
            quizTimerService.cancelAllTimersForRoom(roomCode);

            // 방 취소
            quizBattleService.cancelRoom(roomCode);

            // 모든 참가자에게 방이 취소되었음을 알림
            hello.cluebackend.presentation.websocket.quizbattle.dto.RoomCancelledMessage message =
                hello.cluebackend.presentation.websocket.quizbattle.dto.RoomCancelledMessage.builder()
                    .roomCode(roomCode)
                    .reason("host_disconnected")
                    .status("cancelled")
                    .message("Room has been cancelled because the host disconnected")
                    .build();

            messagingTemplate.convertAndSend("/topic/quiz/" + roomCode + "/game", message);

            log.warn("Host {} disconnected from room {}, room cancelled and all participants removed", hostId, roomCode);

          } else {
            // 학생(참가자)이 연결 끊김
            java.util.UUID userId = redisService.getUserIdBySessionId(roomCode, sessionId);
            System.out.println("####### STUDENT disconnected, user id: " + userId);

            if (userId != null) {
              System.out.println("############################# SUCCESS #############################");
              quizBattleService.leaveRoomBySessionId(sessionId);

              List<QuizParticipant> remainingParticipants = quizBattleService.getParticipants(roomCode);

              ParticipantLeftMessage message = ParticipantLeftMessage.builder()
                      .userId(userId)
                      .totalParticipants(remainingParticipants.size())
                      .allParticipants(remainingParticipants)
                      .status("success")
                      .build();

              messagingTemplate.convertAndSend("/topic/quiz/" + roomCode + "/participants", message);

              log.info("Student {} automatically left room {} due to disconnection", userId, roomCode);
              System.out.println("===========================================================");
            }
          }
        }
      } catch (Exception e) {
        log.error("Error handling WebSocket disconnection for session {}", sessionId, e);
      }
    }
  }
}

package hello.cluebackend.presentation.websocket.quizbattle;

import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class QuizBattleStompClientTest {

    private static final String WS_URL = "ws://localhost:8080/ws-quiz-raw";

    public static void main(String[] args) throws Exception {
        System.out.println("=== Quiz Battle STOMP Client Test ===\n");

        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new StringMessageConverter());

        CountDownLatch connectLatch = new CountDownLatch(1);
        CountDownLatch messageLatch = new CountDownLatch(1);

        StompSessionHandler sessionHandler = new StompSessionHandlerAdapter() {

            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                System.out.println("[CONNECTED] Session ID: " + session.getSessionId());
                System.out.println("[CONNECTED] Headers: " + connectedHeaders);
                connectLatch.countDown();

                session.subscribe("/topic/quiz/rooms", new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return String.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        System.out.println("\n[RECEIVED] /topic/quiz/rooms:");
                        System.out.println(payload);
                        messageLatch.countDown();
                    }
                });

                System.out.println("[SUBSCRIBED] /topic/quiz/rooms");

                session.subscribe("/user/queue/errors", new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return String.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        System.out.println("\n[ERROR] /user/queue/errors:");
                        System.out.println(payload);
                    }
                });

                System.out.println("[SUBSCRIBED] /user/queue/errors");

                // Send create room request
                String createRoomJson = """
                    {
                        "title": "Test Quiz",
                        "topic": "Science",
                        "maxParticipants": 10,
                        "questionCount": 5,
                        "timePerQuestion": 30
                    }
                    """;

                System.out.println("\n[SENDING] Create room request to /app/quiz/create");
                System.out.println(createRoomJson);

                session.send("/app/quiz/create", createRoomJson);
            }

            @Override
            public void handleException(StompSession session, StompCommand command,
                                        StompHeaders headers, byte[] payload, Throwable exception) {
                System.err.println("[EXCEPTION] Command: " + command);
                System.err.println("[EXCEPTION] Message: " + exception.getMessage());
                exception.printStackTrace();
            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                System.err.println("[TRANSPORT ERROR] " + exception.getMessage());
                if (exception.getMessage().contains("Connection refused")) {
                    System.err.println("\n서버가 실행 중인지 확인하세요!");
                }
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("[FRAME] Headers: " + headers);
                System.out.println("[FRAME] Payload: " + payload);
            }
        };

        System.out.println("[CONNECTING] " + WS_URL + "\n");

        try {
            StompSession session = stompClient.connectAsync(WS_URL, sessionHandler)
                    .get(10, TimeUnit.SECONDS);

            // Wait for connection
            boolean connected = connectLatch.await(10, TimeUnit.SECONDS);
            if (!connected) {
                System.err.println("Connection timeout!");
                return;
            }

            // Wait for message or timeout
            System.out.println("\n[WAITING] Waiting for response (30 seconds)...\n");
            boolean received = messageLatch.await(30, TimeUnit.SECONDS);

            if (!received) {
                System.out.println("\n[TIMEOUT] No message received.");
                System.out.println("This might be due to authentication requirements.");
            }

            // Interactive mode
            System.out.println("\n=== Interactive Mode ===");
            System.out.println("Commands:");
            System.out.println("  1. Create room");
            System.out.println("  2. Join room <roomCode>");
            System.out.println("  3. Start quiz <roomCode>");
            System.out.println("  4. Get rankings <roomCode>");
            System.out.println("  q. Quit");
            System.out.println();

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine().trim();

                if ("q".equalsIgnoreCase(input)) {
                    break;
                } else if ("1".equals(input)) {
                    String json = """
                        {"title":"Interactive Quiz","topic":"General","maxParticipants":10,"questionCount":5,"timePerQuestion":30}
                        """;
                    session.send("/app/quiz/create", json);
                    System.out.println("Sent create room request");
                } else if (input.startsWith("2 ")) {
                    String roomCode = input.substring(2).trim();
                    subscribeToRoom(session, roomCode);
                    session.send("/app/quiz/join/" + roomCode, "{}");
                    System.out.println("Sent join request for room: " + roomCode);
                } else if (input.startsWith("3 ")) {
                    String roomCode = input.substring(2).trim();
                    session.send("/app/quiz/start/" + roomCode, "{}");
                    System.out.println("Sent start quiz request for room: " + roomCode);
                } else if (input.startsWith("4 ")) {
                    String roomCode = input.substring(2).trim();
                    session.send("/app/quiz/rankings/" + roomCode, "{}");
                    System.out.println("Sent rankings request for room: " + roomCode);
                } else {
                    System.out.println("Unknown command: " + input);
                }

                // Small delay to see responses
                Thread.sleep(1000);
            }

            session.disconnect();
            System.out.println("\n[DISCONNECTED]");

        } catch (Exception e) {
            System.err.println("[ERROR] " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void subscribeToRoom(StompSession session, String roomCode) {
        // Subscribe to participants
        session.subscribe("/topic/quiz/" + roomCode + "/participants", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("\n[PARTICIPANTS] " + payload);
            }
        });

        // Subscribe to game events
        session.subscribe("/topic/quiz/" + roomCode + "/game", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("\n[GAME] " + payload);
            }
        });

        // Subscribe to rankings
        session.subscribe("/topic/quiz/" + roomCode + "/rankings", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("\n[RANKINGS] " + payload);
            }
        });

        System.out.println("Subscribed to room: " + roomCode);
    }
}

package hello.cluebackend.presentation.api.quizbattle;

import hello.cluebackend.application.user.dto.oauth2.CustomOAuth2User;
import hello.cluebackend.domain.quizbattle.model.QuizParticipant;
import hello.cluebackend.domain.quizbattle.model.QuizRoom;
import hello.cluebackend.domain.quizbattle.model.QuizRoomStatus;
import hello.cluebackend.domain.quizbattle.service.QuizBattleService;
import hello.cluebackend.infrastructure.persistence.quizroom.QuizRoomJpaRepository;
import hello.cluebackend.presentation.api.quizbattle.dto.QuizRoomDetailResponse;
import hello.cluebackend.presentation.api.quizbattle.dto.QuizRoomListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Quiz Battle", description = "Quiz Battle room management APIs")
public class QuizBattleController {

    private final QuizBattleService quizBattleService;
    private final QuizRoomJpaRepository quizRoomRepository;

    @GetMapping("/rooms/active")
    @Operation(summary = "Get all active quiz rooms", description = "Returns all rooms in WAITING or IN_PROGRESS status")
    public ResponseEntity<List<QuizRoomListResponse>> getActiveRooms() {
        List<QuizRoom> rooms = quizRoomRepository.findByStatusIn(
                List.of(QuizRoomStatus.WAITING, QuizRoomStatus.IN_PROGRESS)
        );

        List<QuizRoomListResponse> response = rooms.stream()
                .map(room -> QuizRoomListResponse.builder()
                        .roomCode(room.getRoomCode())
                        .title(room.getTitle())
                        .hostName(room.getHost().getUsername())
                        .status(room.getStatus().name())
                        .maxParticipants(room.getMaxParticipants())
                        .currentParticipants(quizBattleService.getParticipants(room.getRoomCode()).size())
                        .questionCount(room.getQuestionCount())
                        .timePerQuestion(room.getTimePerQuestion())
                        .createdAt(room.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/rooms/{roomCode}")
    @Operation(summary = "Get room details", description = "Returns detailed information about a specific room")
    public ResponseEntity<QuizRoomDetailResponse> getRoomDetails(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @PathVariable String roomCode
            ) {
        try {
            QuizRoom room = quizBattleService.getRoom(roomCode);
            List<QuizParticipant> participants = quizBattleService.getParticipants(roomCode);

            QuizRoomDetailResponse response = QuizRoomDetailResponse.builder()
                    .roomCode(room.getRoomCode())
                    .title(room.getTitle())
                    .topic(room.getTopic())
                    .hostId(room.getHost().getUserId())
                    .hostName(room.getHost().getUsername())
                    .status(room.getStatus().name())
                    .maxParticipants(room.getMaxParticipants())
                    .questionCount(room.getQuestionCount())
                    .timePerQuestion(room.getTimePerQuestion())
                    .participants(participants)
                    .currentParticipants(participants.size())
                    .createdAt(room.getCreatedAt())
                    .startedAt(room.getStartedAt())
                    .finishedAt(room.getFinishedAt())
                    .build();

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/rooms/host/{hostId}")
    @Operation(summary = "Get rooms by host", description = "Returns all rooms created by a specific user")
    public ResponseEntity<List<QuizRoomListResponse>> getRoomsByHost(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @PathVariable UUID hostId
    ) {
        List<QuizRoom> rooms = quizRoomRepository.findByHostId(hostId);

        List<QuizRoomListResponse> response = rooms.stream()
                .map(room -> QuizRoomListResponse.builder()
                        .roomCode(room.getRoomCode())
                        .title(room.getTitle())
                        .hostName(room.getHost().getUsername())
                        .status(room.getStatus().name())
                        .maxParticipants(room.getMaxParticipants())
                        .currentParticipants(quizBattleService.getParticipants(room.getRoomCode()).size())
                        .questionCount(room.getQuestionCount())
                        .timePerQuestion(room.getTimePerQuestion())
                        .createdAt(room.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/rooms/classroom/{classRoomId}")
    @Operation(summary = "Get rooms by classroom", description = "Returns active rooms associated with a specific classroom")
    public ResponseEntity<List<QuizRoomListResponse>> getRoomsByClassroom(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @PathVariable UUID classRoomId
    ) {
        List<QuizRoom> rooms = quizRoomRepository.findByClassRoomIdAndStatusIn(
                classRoomId,
                List.of(QuizRoomStatus.WAITING, QuizRoomStatus.IN_PROGRESS)
        );

        List<QuizRoomListResponse> response = rooms.stream()
                .map(room -> QuizRoomListResponse.builder()
                        .roomCode(room.getRoomCode())
                        .title(room.getTitle())
                        .hostName(room.getHost().getUsername())
                        .status(room.getStatus().name())
                        .maxParticipants(room.getMaxParticipants())
                        .currentParticipants(quizBattleService.getParticipants(room.getRoomCode()).size())
                        .questionCount(room.getQuestionCount())
                        .timePerQuestion(room.getTimePerQuestion())
                        .createdAt(room.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/rooms/{roomCode}/joinable")
    @Operation(summary = "Check if room is joinable", description = "Returns whether a room can be joined")
    public ResponseEntity<Boolean> isRoomJoinable(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @PathVariable String roomCode
    ) {
        try {
            QuizRoom room = quizBattleService.getRoom(roomCode);
            boolean canJoin = room.canJoin() &&
                    quizBattleService.getParticipants(roomCode).size() < room.getMaxParticipants();
            return ResponseEntity.ok(canJoin);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(false);
        }
    }
}

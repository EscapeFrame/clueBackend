package hello.cluebackend.domain.quizbattle.model;

import hello.cluebackend.domain.classroom.model.ClassRoom;
import hello.cluebackend.domain.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "quiz_room")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "quiz_room_id", nullable = false, updatable = false)
    private UUID quizRoomId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 20)
    private String roomCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private UserEntity host;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_room_id")
    private ClassRoom classRoom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuizRoomStatus status;

    @Column(nullable = false)
    private Integer maxParticipants;

    @Column(nullable = false)
    private Integer questionCount;

    @Column(nullable = false)
    private Integer timePerQuestion;

    @Column(length = 500)
    private String topic;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime finishedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = QuizRoomStatus.WAITING;
        }
    }

    public void start() {
        this.status = QuizRoomStatus.IN_PROGRESS;
        this.startedAt = LocalDateTime.now();
    }

    public void finish() {
        this.status = QuizRoomStatus.FINISHED;
        this.finishedAt = LocalDateTime.now();
    }

    public void cancel() {
        this.status = QuizRoomStatus.CANCELLED;
    }

    public boolean isHost(UUID userId) {
        return this.host.getUserId().equals(userId);
    }

    public boolean canJoin() {
        return this.status == QuizRoomStatus.WAITING;
    }

    public boolean isActive() {
        return this.status == QuizRoomStatus.WAITING || this.status == QuizRoomStatus.IN_PROGRESS;
    }
}

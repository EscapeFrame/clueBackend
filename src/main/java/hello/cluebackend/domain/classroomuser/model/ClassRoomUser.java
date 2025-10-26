package hello.cluebackend.domain.classroomuser.model;

import hello.cluebackend.domain.classroom.model.ClassRoom;
import hello.cluebackend.domain.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name="class_room_user")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoomUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "class_room_user_id", nullable = false, updatable = false)
    private UUID classRoomUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_room_id", nullable = false)
    private ClassRoom classRoom;
}

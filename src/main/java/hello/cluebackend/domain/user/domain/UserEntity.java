package hello.cluebackend.domain.user.domain;

import hello.cluebackend.domain.classroomuser.domain.ClassRoomUser;
import hello.cluebackend.domain.user.presentation.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_entity")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id", nullable = false)
    private Long userId;

    @ColumnDefault("-1")
    @Column(name="class_code")
    private int classCode;

    @Column(name="user_name", nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ClassRoomUser> classRoomUserList = new ArrayList<>();

    public UserEntity(int classCode, String username, String email, Role role) {
        this.classCode = classCode;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public UserDto toUserDTO() {
        return UserDto.builder()
                .userId(userId)
                .classCode(classCode)
                .username(username)
                .email(email)
                .role(role)
                .createdAt(createdAt)
                .build();
    }
}

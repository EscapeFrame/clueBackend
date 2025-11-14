package hello.cluebackend.domain.user.model;

import hello.cluebackend.application.user.dto.RegisterUserDto;
import hello.cluebackend.application.user.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.UUID;

@ToString
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_entity")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="user_id", nullable = false, updatable = false)
    private UUID userId;

    @ColumnDefault("-1")
    @Column(name="class_code")
    private int classCode;

    private int grade;

    @Column(name="class")
    private int classNo;

    private int number;

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

    public static UserEntity create(UserDto userDto, RegisterUserDto registerUserDto) {
        UserEntity user = new UserEntity();
        user.username = userDto.getUsername();
        user.email = userDto.getEmail();
        user.role = userDto.getRole();
        user.grade = registerUserDto.getGrade();
        user.classNo = registerUserDto.getClassNo();
        user.number = registerUserDto.getNumber();
        return user;
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

    public boolean isTeacher() {
      return this.role == Role.TEACHER;
    }
}

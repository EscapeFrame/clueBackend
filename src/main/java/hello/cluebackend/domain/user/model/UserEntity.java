package hello.cluebackend.domain.user.model;

import hello.cluebackend.application.user.dto.RegisterUserDto;
import hello.cluebackend.application.user.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@ToString
@Entity
@Getter
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

    private Integer grade;

    @Column(name="class")
    private Integer classNo;

    private Integer number;

    @Column(name="user_name", nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String value;

    @Column(nullable = true)
    private String contentType;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static UserEntity create(UserDto userDto, RegisterUserDto registerUserDto, String storedFileName,  MultipartFile image) throws IOException {
        UserEntity user = new UserEntity();
        user.username = userDto.getUsername();
        user.email = userDto.getEmail();
        user.role = userDto.getRole();
        user.grade = registerUserDto.getGrade();
        user.classNo = registerUserDto.getClassNo();
        user.number = registerUserDto.getNumber();
        user.value = storedFileName;
        user.contentType = image.getContentType();
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

    public void update(String email, String username) {
        if(!this.email.equals(email)) this.email = email;
        if(!this.username.equals(username)) this.username = username;
    }
}

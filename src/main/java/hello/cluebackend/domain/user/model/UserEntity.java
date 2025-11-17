package hello.cluebackend.domain.user.model;

import hello.cluebackend.application.user.dto.request.RegisterUserDto;
import hello.cluebackend.application.user.dto.request.UpdateUserDto;
import hello.cluebackend.application.user.dto.response.UserDto;
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

    @Column(nullable = true)
    private String value;

    @Column(nullable = true)
    private String contentType;

    @Column(nullable = true)
    private String description;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static UserEntity create(UserDto userDto, RegisterUserDto registerUserDto, String storedFileName,  MultipartFile image) throws IOException {
        UserEntity user = new UserEntity();
        user.username = registerUserDto.getUsername();
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
                .grade(grade)
                .classNo(classNo)
                .number(number)
                .description(description)
                .build();
    }

    public boolean isTeacher() {
      return this.role == Role.TEACHER;
    }

    public void update(String email, String username) {
        if(!this.email.equals(email)) this.email = email;
        if(!this.username.equals(username)) this.username = username;
    }

    public void updateProfile(UpdateUserDto userDto) {
        if(userDto.getUsername() != null) this.username = userDto.getUsername();
        if(userDto.getGrade() != null) this.grade = userDto.getGrade();
        if(userDto.getClassNo() != null) this.classNo = userDto.getClassNo();
        if(userDto.getNumber() != null) this.number = userDto.getNumber();
        if(userDto.getDescription() != null) this.description = userDto.getDescription();
    }
}

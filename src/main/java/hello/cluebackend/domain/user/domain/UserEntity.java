package hello.cluebackend.domain.user.domain;

import hello.cluebackend.domain.user.presentation.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "UserEntity")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ColumnDefault("-1")
    private int studentId;

    private String username;

    private String email;

    private String role;

    private String addition;

    @CreatedDate
    private LocalDateTime createdAt;

    public UserEntity(int studentId, String username, String addition, String email, String role) {
        this.studentId = studentId;
        this.username = username;
        this.addition = addition;
        this.email = email;
        this.role = role;
    }

    public UserDTO toUserDTO() {
        return new UserDTO(email, role, username, studentId, addition);
    }
}

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
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id", nullable = false)
    private Long userId;

    @ColumnDefault("-1")
    private int studentId;

    @Column(name="user_name", nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String addition;

    @CreatedDate
    @Column(nullable = false)
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

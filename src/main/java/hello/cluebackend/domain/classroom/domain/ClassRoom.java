package hello.cluebackend.domain.classroom.domain;

import hello.cluebackend.domain.classroom.presentation.dto.ClassRoomDTO;
import hello.cluebackend.domain.classroomuser.domain.ClassRoomUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="class_room")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long classRoomId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "classRoom", cascade = CascadeType.ALL)
    private List<ClassRoomUser> classRoomUserList = new ArrayList<>();

    public ClassRoomDTO toDTO() {
        return ClassRoomDTO.builder()
                .classRoomId(classRoomId)
                .name(name)
                .description(description)
                .code(code)
                .createdAt(createdAt)
                .build();
    }
}

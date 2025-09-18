package hello.cluebackend.domain.classroom.domain;

import hello.cluebackend.domain.classroom.presentation.dto.ClassRoomCardDto;
import hello.cluebackend.domain.classroom.presentation.dto.ClassRoomDto;
import hello.cluebackend.domain.classroomuser.domain.ClassRoomUser;
import hello.cluebackend.domain.directory.domain.Directory;
import hello.cluebackend.domain.document.domain.Document;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="class_room")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassRoom {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "class_room_id", nullable = false, updatable = false)
    private UUID classRoomId;

    @Column(nullable = false, length = 40)
    private String name;

    @Column(nullable = false, length = 150)
    private String description;

    @Column(nullable = false, length = 30)
    private String sort;

    @Column(nullable = false)
    private String target;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean isActivation;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "classRoom", cascade = CascadeType.ALL)
    private List<ClassRoomUser> classRoomUserList;

    @OneToMany(mappedBy = "classRoom", cascade = CascadeType.ALL)
    private List<Directory>  directoryList;

    @OneToMany(mappedBy = "classRoom", cascade = CascadeType.ALL)
    private List<Document> documentList;

    public ClassRoomDto toDTO() {
        return ClassRoomDto.builder()
                .classRoomId(classRoomId)
                .name(name)
                .description(description)
                .sort(sort)
                .target(target)
                .code(code)
                .createdAt(createdAt)
                .isActivation(isActivation)
                .build();
    }

    public ClassRoomCardDto toCardDTO() {
        return ClassRoomCardDto.builder()
                .classRoomId(classRoomId)
                .name(name)
                .sort(sort)
                .target(target)
                .studentCount(classRoomUserList.size())
                .isActivation(isActivation)
                .build();
    }
}

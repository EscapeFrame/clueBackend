package hello.cluebackend.domain.classroom.model;

import hello.cluebackend.application.classroom.dto.ClassRoomCardDto;
import hello.cluebackend.application.classroom.dto.ClassRoomDto;
import hello.cluebackend.domain.assignment.model.Assignment;
import hello.cluebackend.domain.classroomuser.model.ClassRoomUser;
import hello.cluebackend.domain.directory.model.Directory;
import hello.cluebackend.domain.document.model.Document;
import hello.cluebackend.domain.user.model.Role;
import hello.cluebackend.domain.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="class_room")
@Getter
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
    private List<ClassRoomUser> classRoomUserList = new ArrayList<>();

    @OneToMany(mappedBy = "classRoom", cascade = CascadeType.ALL)
    private List<Directory>  directoryList = new ArrayList<>();

    @OneToMany(mappedBy = "classRoom", cascade = CascadeType.ALL)
    private List<Document> documentList = new ArrayList<>();

    @OneToMany(mappedBy = "classRoom", cascade = CascadeType.ALL)
    private List<Assignment> assignmentList = new ArrayList<>();

    public ClassRoomDto toDTO() {
        List<String> teacherNames = classRoomUserList.stream()
                .map(cu -> cu.getUser().getUsername())
                .toList();

        return ClassRoomDto.builder()
                .classRoomId(classRoomId)
                .name(name)
                .description(description)
                .sort(sort)
                .target(target)
                .code(code)
                .createdAt(createdAt)
                .isActivation(isActivation)
                .teacherNames(teacherNames)
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

    public void update(ClassRoomDto classRoomDTO) {
        this.name = classRoomDTO.getName();
        this.description = classRoomDTO.getDescription();
        this.sort = classRoomDTO.getSort();
        this.target = classRoomDTO.getTarget();
        this.isActivation = classRoomDTO.getIsActivation();
    }

    public boolean isTeacher(UserEntity user) {
      return user.getRole() == Role.TEACHER;
    }
}

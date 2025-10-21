package hello.cluebackend.domain.student.document.domain;

import hello.cluebackend.domain.student.assignment.domain.FileType;
import hello.cluebackend.domain.student.classroom.domain.ClassRoom;
import hello.cluebackend.domain.student.directory.domain.Directory;
import hello.cluebackend.domain.student.document.controller.dto.DocumentDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="document_id", nullable = false, updatable = false)
    private UUID documentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_room_id",  nullable = false)
    private ClassRoom classRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "directory_id",  nullable = false)
    private Directory directory;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // FILE, URL
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FileType type;

    // 실제 파일이면 S3 Key, URL이면 링크
    @Column(nullable = false)
    private String value;

    // 파일일 경우 메타데이터
    private String originalFileName;
    private String contentType;
    private Long size;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public DocumentDto toDto() {
        return DocumentDto.builder()
                .documentId(documentId)
                .classRoom(classRoom)
                .directory(directory)
                .title(title)
                .type(type)
                .createdAt(createdAt)
                .build();
    }

    public void updateDetails(String title) {
        if (title != null) this.title = title;
    }
}

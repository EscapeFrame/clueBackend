package hello.cluebackend.domain.document.domain;

import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.directory.domain.Directory;
import hello.cluebackend.domain.document.presentation.dto.DocumentDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="document_id", nullable = false)
    private Long documentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_room_id",  nullable = false)
    private ClassRoom classRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "directory_id",  nullable = false)
    private Directory directory;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int type;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

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
                .content(content)
                .createdAt(createdAt)
                .build();
    }
}

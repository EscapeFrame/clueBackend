package hello.cluebackend.application.document.dto;

import hello.cluebackend.domain.assignment.model.FileType;
import hello.cluebackend.domain.classroom.model.ClassRoom;
import hello.cluebackend.domain.directory.model.Directory;
import hello.cluebackend.domain.document.model.Document;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDto {
    private UUID documentId;
    private ClassRoom classRoom;
    private Directory directory;
    private String title;
    private FileType type;
    private String content;
    private LocalDateTime createdAt;

    public Document toEntity() {
        return Document.builder()
                .documentId(documentId)
                .classRoom(classRoom)
                .directory(directory)
                .title(title)
                .type(type)
                .createdAt(createdAt)
                .build();
    }
}

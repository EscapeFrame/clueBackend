package hello.cluebackend.domain.document.presentation.dto;

import hello.cluebackend.domain.assignment.domain.FileType;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.directory.domain.Directory;
import hello.cluebackend.domain.document.domain.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
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

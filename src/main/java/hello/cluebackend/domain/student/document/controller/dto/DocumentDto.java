package hello.cluebackend.domain.student.document.controller.dto;

import hello.cluebackend.domain.student.assignment.domain.FileType;
import hello.cluebackend.domain.student.classroom.domain.ClassRoom;
import hello.cluebackend.domain.student.directory.domain.Directory;
import hello.cluebackend.domain.student.document.domain.Document;
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

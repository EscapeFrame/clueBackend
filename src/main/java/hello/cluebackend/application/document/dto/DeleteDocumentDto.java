package hello.cluebackend.application.document.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteDocumentDto {
    private UUID  documentId;
}

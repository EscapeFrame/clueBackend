package hello.cluebackend.domain.document.controller.dto;

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

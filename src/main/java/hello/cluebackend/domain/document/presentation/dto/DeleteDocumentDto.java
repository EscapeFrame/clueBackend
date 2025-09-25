package hello.cluebackend.domain.document.presentation.dto;

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

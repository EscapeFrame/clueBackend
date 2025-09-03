package hello.cluebackend.domain.document.presentation.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RequestDocumentDto {
    private UUID documentId;
    private String title;
    private int type;

}

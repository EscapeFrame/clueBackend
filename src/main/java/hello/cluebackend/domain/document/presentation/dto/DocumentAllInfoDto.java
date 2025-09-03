package hello.cluebackend.domain.document.presentation.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentAllInfoDto {
    private UUID documentId;
    private String title;
    private LocalDateTime createdAt;
}

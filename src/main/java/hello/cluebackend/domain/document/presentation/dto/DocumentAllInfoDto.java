package hello.cluebackend.domain.document.presentation.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentAllInfoDto {
    private Long documentId;
    private String title;
    private LocalDateTime createdAt;
}

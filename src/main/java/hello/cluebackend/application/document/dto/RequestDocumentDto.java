package hello.cluebackend.application.document.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RequestDocumentDto {
    private String title;
}
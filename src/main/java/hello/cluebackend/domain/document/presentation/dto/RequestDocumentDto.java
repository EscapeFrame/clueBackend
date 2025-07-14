package hello.cluebackend.domain.document.presentation.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RequestDocumentDto {

    private String title;
    private int type;

}

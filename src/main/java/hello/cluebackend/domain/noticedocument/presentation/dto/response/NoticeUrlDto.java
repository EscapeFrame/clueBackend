package hello.cluebackend.domain.noticedocument.presentation.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NoticeUrlDto {
    private String value;
    private String title;
}
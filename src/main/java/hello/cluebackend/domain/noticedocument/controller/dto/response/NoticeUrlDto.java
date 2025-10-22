package hello.cluebackend.domain.noticedocument.controller.dto.response;

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
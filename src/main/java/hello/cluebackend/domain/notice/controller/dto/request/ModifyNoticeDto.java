package hello.cluebackend.domain.notice.controller.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyNoticeDto {
    private String title;
    private String content;
}

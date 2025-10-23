package hello.cluebackend.domain.notice.controller.dto.request;

import hello.cluebackend.domain.notice.domain.NoticeType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyNoticeDto {
    private NoticeType type;
    private String title;
    private String content;
}

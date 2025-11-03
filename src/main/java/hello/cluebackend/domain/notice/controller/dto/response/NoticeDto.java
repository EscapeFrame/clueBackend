package hello.cluebackend.domain.notice.controller.dto.response;

import hello.cluebackend.domain.notice.model.NoticeType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeDto {
    private NoticeType type;
    private UUID noticeId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
}
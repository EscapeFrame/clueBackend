package hello.cluebackend.domain.notice.presentation.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeDto {
    private UUID noticeId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
}

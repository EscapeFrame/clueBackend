package hello.cluebackend.domain.notice.controller.dto.response;

import hello.cluebackend.domain.notice.domain.NoticeType;
import hello.cluebackend.domain.noticedocument.controller.dto.response.NoticeDocumentDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeInfoDto {
    private NoticeType type;
    private UUID noticeId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    List<NoticeDocumentDto> noticeDocuments = new ArrayList<>();
}

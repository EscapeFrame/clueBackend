package hello.cluebackend.domain.notice.presentation.dto.response;

import hello.cluebackend.domain.noticedocument.presentation.dto.response.NoticeDocumentDto;
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
    private UUID noticeId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    List<NoticeDocumentDto> noticeDocuments = new ArrayList<>();
}

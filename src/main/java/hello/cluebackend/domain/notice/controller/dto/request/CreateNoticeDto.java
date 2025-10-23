package hello.cluebackend.domain.notice.controller.dto.request;

import hello.cluebackend.domain.document.controller.dto.UrlDto;
import hello.cluebackend.domain.notice.domain.NoticeType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNoticeDto {
    private NoticeType type;
    private String title;
    private String content;
    private UUID classRoomId;
    private List<NoticeFileDto> fileInfo = new ArrayList<>();
    private List<UrlDto> urls = new ArrayList<>();
}
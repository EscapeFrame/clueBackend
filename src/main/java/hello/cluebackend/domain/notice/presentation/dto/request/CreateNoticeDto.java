package hello.cluebackend.domain.notice.presentation.dto.request;

import hello.cluebackend.domain.document.presentation.dto.UrlDto;
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
    private String title;
    private String content;
    private UUID classRoomId;
    List<NoticeFileDto> fileInfo = new ArrayList<>();
    private List<UrlDto> urls = new ArrayList<>();
}
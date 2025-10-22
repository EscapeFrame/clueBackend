package hello.cluebackend.domain.notice.presentation.dto.request;

import hello.cluebackend.domain.document.presentation.dto.UrlDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddNoticeDto {
    private List<NoticeFileDto> fileInfo = new ArrayList<>();
    private List<UrlDto> urls = new ArrayList<>();
}

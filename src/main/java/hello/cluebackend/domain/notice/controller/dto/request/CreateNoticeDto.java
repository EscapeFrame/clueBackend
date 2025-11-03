package hello.cluebackend.domain.notice.controller.dto.request;

import hello.cluebackend.application.document.dto.UrlDto;
import hello.cluebackend.domain.notice.model.NoticeType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNoticeDto {
    private NoticeType type;
    private String title;
    private String content;
    private List<NoticeFileDto> fileInfo = new ArrayList<>();
    private List<UrlDto> urls = new ArrayList<>();
}
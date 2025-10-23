package hello.cluebackend.domain.noticedocument.controller.dto.response;

import lombok.*;
import org.springframework.core.io.Resource;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeDownloadDto {
    private Resource resource;
    private String original;
    private String contentType;
}

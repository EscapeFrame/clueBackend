package hello.cluebackend.domain.noticedocument.presentation.dto.response;

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

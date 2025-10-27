package hello.cluebackend.application.document.dto;

import lombok.*;
import org.springframework.core.io.Resource;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DownloadDto {
    private Resource resource;
    private String original;
    private String contentType;
}

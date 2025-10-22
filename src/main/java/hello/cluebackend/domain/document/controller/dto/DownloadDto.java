package hello.cluebackend.domain.document.controller.dto;

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

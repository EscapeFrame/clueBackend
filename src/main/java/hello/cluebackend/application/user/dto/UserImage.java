package hello.cluebackend.application.user.dto;

import lombok.*;
import org.springframework.core.io.Resource;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserImage {
    private String contentType;
    private Resource resource;
}

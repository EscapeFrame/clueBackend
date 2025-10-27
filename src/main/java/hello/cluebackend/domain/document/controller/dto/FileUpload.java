package hello.cluebackend.domain.document.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FileUpload {

    private String originalFileName;
    // uuid 포함
    private String storedFileName;

    private String fullPath;

}

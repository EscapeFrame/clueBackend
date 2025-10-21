package hello.cluebackend.domain.student.directory.controller.dto;

import hello.cluebackend.domain.student.document.controller.dto.DocumentAllInfoDto;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DirectoryAllInfoDto {
    private UUID directoryId;
    private String directoryName;
    private int directoryOrder;
    private List<DocumentAllInfoDto> documentList;
}
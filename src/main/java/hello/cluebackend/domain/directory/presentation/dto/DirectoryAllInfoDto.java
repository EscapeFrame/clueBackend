package hello.cluebackend.domain.directory.presentation.dto;

import hello.cluebackend.domain.document.presentation.dto.DocumentAllInfoDto;
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
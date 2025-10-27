package hello.cluebackend.domain.noticedocument.controller.dto.response;

import hello.cluebackend.domain.noticedocument.domain.FileType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeDocumentDto {
    private UUID noticeDocumentId;
    private String title;
    private FileType type;
}

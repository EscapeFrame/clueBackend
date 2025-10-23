package hello.cluebackend.domain.noticedocument.controller.dto.response;

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
}

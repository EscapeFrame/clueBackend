package hello.cluebackend.application.document.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InfoDto {
    private UUID classRoomId;
    private UUID directoryId;
    private List<UrlDto> urls = new ArrayList<>();
}

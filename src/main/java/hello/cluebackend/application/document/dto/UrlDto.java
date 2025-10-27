package hello.cluebackend.application.document.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UrlDto {
    private String value;
    private String title;
}
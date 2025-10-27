package hello.cluebackend.domain.document.controller.dto;

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
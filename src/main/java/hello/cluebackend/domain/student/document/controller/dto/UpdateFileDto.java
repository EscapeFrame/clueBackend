package hello.cluebackend.domain.student.document.controller.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateFileDto {
    private UUID documentId;
    private String title;
}
package hello.cluebackend.domain.assignment.presentation.dto;

import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.user.domain.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@ToString @Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentAttachmentDto {
    private Long assignmentAttachmentId;
    private Assignment assignment;
    private UserEntity user;
    private String originalFileName;
    private String storedFileName;
    private String filePath;
    private Integer fileSize;
    private LocalDateTime updateDate;
}
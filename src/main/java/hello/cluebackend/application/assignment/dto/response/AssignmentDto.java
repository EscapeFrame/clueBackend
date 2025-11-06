package hello.cluebackend.application.assignment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import hello.cluebackend.domain.assignment.model.Assignment;
import hello.cluebackend.domain.assignment.model.AssignmentAttachment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AssignmentDto(
        UUID assignmentId,
        String title,
        String content,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul") LocalDateTime startDate,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul") LocalDateTime endDate,
        String userName,

        // 과제 첨부 데이터
        List<AssignmentAttachmentDto> attachmentDtos
) {
  public static AssignmentDto from(Assignment assignment, List<AssignmentAttachment> assignmentAttachments){

    List<AssignmentAttachmentDto> assignmentResponseDtos = assignmentAttachments.stream()
            .map(aa -> AssignmentAttachmentDto.from(aa))
            .toList();

    return new AssignmentDto(
      assignment.getAssignmentId(),
      assignment.getTitle(),
      assignment.getContent(),
      assignment.getStartDate(),
      assignment.getEndDate(),
      assignment.getUser().getUsername(),
      assignmentResponseDtos
    );
  }
}
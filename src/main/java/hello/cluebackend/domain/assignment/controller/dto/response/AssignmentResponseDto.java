package hello.cluebackend.domain.assignment.controller.dto.response;

import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.assignment.domain.AssignmentAttachment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AssignmentResponseDto(
        UUID assignmentId,
        String title,
        String content,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String userName,

        // 과제 첨부 데이터
        List<AssignmentAttachmentDto> attachmentDtos
) {
  public static AssignmentResponseDto from(Assignment assignment, List<AssignmentAttachment> assignmentAttachments){

    List<AssignmentAttachmentDto> assignmentResponseDtos = assignmentAttachments.stream()
            .map(aa -> AssignmentAttachmentDto.from(aa))
            .toList();

    return new AssignmentResponseDto(
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
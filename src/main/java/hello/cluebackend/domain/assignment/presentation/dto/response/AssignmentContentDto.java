package hello.cluebackend.domain.assignment.presentation.dto.response;

public record AssignmentContentDto (
  Long assignmentContentId,
  String fileName,
  String assignmentLink,
  int submitType
) {}

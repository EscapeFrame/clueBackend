package hello.cluebackend.domain.assignment.api;

import hello.cluebackend.domain.assignment.api.dto.response.*;
import hello.cluebackend.domain.assignment.application.AssignmentCommandService;
import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.assignment.domain.AssignmentAttachment;
import hello.cluebackend.domain.assignment.exception.AccessDeniedException;
import hello.cluebackend.domain.classroomuser.application.ClassroomUserService;
import hello.cluebackend.domain.submission.api.dto.response.SubmissionAttachmentDto;
import hello.cluebackend.domain.submission.application.SubmissionCommandService;
import hello.cluebackend.global.common.annotation.CurrentUser;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
@Slf4j
public class AssignmentCommandController {
  private final ClassroomUserService classroomUserService;
  private final AssignmentCommandService assignmentCommandService;
  private final SubmissionCommandService submissionCommandService;

  // 과제 단일 조회
  @GetMapping("/{assignmentId}")
  public ResponseEntity<AssignmentResponseDto> getAssignment(
          @CurrentUser Long userId,
          @PathVariable Long assignmentId
  ) {
    AssignmentResponseDto result = assignmentCommandService.findById(assignmentId);
    Assignment assignment = assignmentCommandService.findByIdOrThrow(assignmentId);
    Long classroomId = assignment.getClassRoom().getClassRoomId();
    if (!classroomUserService.isUserInClassroom(classroomId, userId)) {
      throw new AccessDeniedException("해당 수업실에 속하지 않은 유저입니다.");
    }
    return ResponseEntity.ok(result);
  }

  // 교실 과제 전체 조회
  @GetMapping("/{classId}/all")
  public ResponseEntity<List<AssignmentResponseDto>> getAllClassroomAssignment(
          @CurrentUser Long userId,
          @PathVariable Long classId
  ) {
    List<AssignmentResponseDto> result = assignmentCommandService.findAllById(userId,classId);

    if (!classroomUserService.isUserInClassroom(classId, userId)) {
        throw new AccessDeniedException("해당 수업실에 속하지 않은 유저입니다.");
    }

    return ResponseEntity.ok(result);
  }

  // 메인 페이지 모든 과제 조회
  @GetMapping("/me")
  public ResponseEntity<List<GetAllAssignmentDto>> getAllAssignments(@CurrentUser Long userId) {
    List<GetAllAssignmentDto> result = assignmentCommandService.findAllAssignmentMe(userId);
    return ResponseEntity.ok(result);
  }

  // 첨부 파일 혹은 링크 전체 조회 (선생, 학생)
  @GetMapping("/{submissionId}/attachment")
  public ResponseEntity<List<SubmissionAttachmentDto>> findAllAssignments(@CurrentUser Long userId, @PathVariable Long submissionId) {
    List<SubmissionAttachmentDto> result = submissionCommandService.findAllAssignment(submissionId);
    return ResponseEntity.ok(result);
  }

  // 첨부 파일 다운로드
  @GetMapping("/{assignmentAttachmentId}/download")
  public ResponseEntity<Resource> assignmentAttachmentDownload(
          @CurrentUser Long userId,
          @PathVariable Long assignmentAttachmentId
  ) throws IOException {
    AssignmentAttachment assignmentAttachment = assignmentCommandService.findAssignmentAttachmentByIdOrderThrow(assignmentAttachmentId);
    Resource resource = assignmentCommandService.downloadAttachment(assignmentAttachment);

    String original = assignmentAttachment.getOriginalFileName();
    String contentType = assignmentAttachment.getContentType();
    MediaType mediaType = (contentType != null) ? MediaType.parseMediaType(contentType) : MediaType.APPLICATION_OCTET_STREAM;
    return ResponseEntity.ok()
            .contentType(mediaType)
            .header(HttpHeaders.CONTENT_DISPOSITION,
                    ContentDisposition.attachment()
                            .filename(original, StandardCharsets.UTF_8)
                            .build()
                            .toString())
            .body(resource);
  }
}

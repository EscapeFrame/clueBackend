package hello.cluebackend.presentation.api.assignment;

import hello.cluebackend.domain.assignment.service.AssignmentCommandService;
import hello.cluebackend.domain.assignment.model.Assignment;
import hello.cluebackend.domain.assignment.exception.AccessDeniedException;
import hello.cluebackend.application.assignment.dto.response.AssignmentDto;
import hello.cluebackend.application.assignment.dto.response.GetAllAssignmentDto;
import hello.cluebackend.domain.classroomuser.service.ClassroomUserService;
import hello.cluebackend.application.user.dto.oauth2.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.cluebackend.application.assignment.dto.response.AssignmentAttachmentDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
@Slf4j
public class AssignmentCommandController {
  private final ClassroomUserService classroomUserService;
  private final AssignmentCommandService assignmentCommandService;

  // 과제 단일 조회
  @GetMapping("/{assignmentId}")
  public ResponseEntity<AssignmentDto> getAssignment(
          @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
          @PathVariable UUID assignmentId
  ) {
    Assignment assignment = assignmentCommandService.findByIdOrThrow(assignmentId);
    UUID classroomId = assignment.getClassRoom().getClassRoomId();
    if (!classroomUserService.isUserInClassroom(classroomId, customOAuth2User.getUserId())) {
      throw new AccessDeniedException("해당 수업실에 속하지 않은 유저입니다.");
    }
    AssignmentDto result = assignmentCommandService.findById(assignmentId);
    return ResponseEntity.ok(result);
  }

  // 교실 과제 전체 조회
  @GetMapping("/{classId}/all")
  public ResponseEntity<List<AssignmentDto>> getAllClassroomAssignment(
          @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
          @PathVariable UUID classId
  ) {
    List<AssignmentDto> result = assignmentCommandService.findAllById(customOAuth2User.getUserId(),classId);

    return ResponseEntity.ok(result);
  }

  // 메인 페이지 모든 과제 조회
  @GetMapping("/me")
  public ResponseEntity<List<GetAllAssignmentDto>> getAllAssignments(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
    List<GetAllAssignmentDto> result = assignmentCommandService.findAllAssignmentMe(customOAuth2User.getUserId());
    return ResponseEntity.ok(result);
  }

  // ------------------------------------------ 첨부 파일 ------------------------------------------ //

  // 첨부 파일 목록 조회
  @GetMapping("/{assignmentId}/attachment")
  public ResponseEntity<List<AssignmentAttachmentDto>> getAttachments(
          @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
          @PathVariable UUID assignmentId
  ) {
    Assignment assignment = assignmentCommandService.findByIdOrThrow(assignmentId);
    UUID classroomId = assignment.getClassRoom().getClassRoomId();
    if (!classroomUserService.isUserInClassroom(classroomId, customOAuth2User.getUserId())) {
      throw new AccessDeniedException("해당 수업실에 속하지 않은 유저입니다.");
    }
    List<AssignmentAttachmentDto> attachments = assignmentCommandService.findAttachmentsByAssignmentId(assignmentId);
    return ResponseEntity.ok(attachments);
  }

  @GetMapping("/{assignmentAttachmentId}/download")
  public ResponseEntity<String> assignmentAttachmentDownload(
          @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
          @PathVariable UUID assignmentAttachmentId
  ) {
    String downloadUrl = assignmentCommandService.getAttachmentDownloadUrl(
            customOAuth2User.getUserId(),
            assignmentAttachmentId
    );

    return ResponseEntity.ok(downloadUrl);
  }
}

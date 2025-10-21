package hello.cluebackend.domain.assignment.controller;

import hello.cluebackend.domain.assignment.service.AssignmentCommandService;
import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.assignment.domain.AssignmentAttachment;
import hello.cluebackend.domain.assignment.exception.AccessDeniedException;
import hello.cluebackend.domain.assignment.controller.dto.response.AssignmentResponseDto;
import hello.cluebackend.domain.assignment.controller.dto.response.GetAllAssignmentDto;
import hello.cluebackend.domain.classroomuser.service.ClassroomUserService;
import hello.cluebackend.domain.user.presentation.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
  public ResponseEntity<AssignmentResponseDto> getAssignment(
          @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
          @PathVariable UUID assignmentId
  ) {
    Assignment assignment = assignmentCommandService.findByIdOrThrow(assignmentId);
    UUID classroomId = assignment.getClassRoom().getClassRoomId();
    if (!classroomUserService.isUserInClassroom(classroomId, customOAuth2User.getUserId())) {
      throw new AccessDeniedException("해당 수업실에 속하지 않은 유저입니다.");
    }
    AssignmentResponseDto result = assignmentCommandService.findById(assignmentId);
    return ResponseEntity.ok(result);
  }

  // 교실 과제 전체 조회
  @GetMapping("/{classId}/all")
  public ResponseEntity<List<AssignmentResponseDto>> getAllClassroomAssignment(
          @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
          @PathVariable UUID classId
  ) {
    List<AssignmentResponseDto> result = assignmentCommandService.findAllById(customOAuth2User.getUserId(),classId);

    return ResponseEntity.ok(result);
  }

  // 메인 페이지 모든 과제 조회
  @GetMapping("/me")
  public ResponseEntity<List<GetAllAssignmentDto>> getAllAssignments(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
    List<GetAllAssignmentDto> result = assignmentCommandService.findAllAssignmentMe(customOAuth2User.getUserId());
    return ResponseEntity.ok(result);
  }

  // ------------------------------------------ 첨부 파일 ------------------------------------------ //

  // 첨부 파일 다운로드
  @GetMapping("/{assignmentAttachmentId}/download")
  public ResponseEntity<Resource> assignmentAttachmentDownload(
          @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
          @PathVariable UUID assignmentAttachmentId
  ) throws IOException {
    AssignmentAttachment assignmentAttachment = assignmentCommandService.findAssignmentAttachmentByIdOrderThrow(customOAuth2User.getUserId(),assignmentAttachmentId);
    Resource resource = assignmentCommandService.downloadAttachment(assignmentAttachment);

    String original = assignmentAttachment.getOriginalFileName();
    String contentType = assignmentAttachment.getContentType();
    MediaType mediaType = (contentType != null) ? MediaType.parseMediaType(contentType) : MediaType.ALL;

    return ResponseEntity.ok()
            .contentType(mediaType)
            .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                            .filename(original, StandardCharsets.UTF_8)
                            .build()
                            .toString())
            .body(resource);
  }
}

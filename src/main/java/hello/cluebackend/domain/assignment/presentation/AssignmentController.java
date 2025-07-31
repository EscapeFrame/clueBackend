package hello.cluebackend.domain.assignment.presentation;


import hello.cluebackend.domain.assignment.presentation.dto.AssignmentAttachmentDto;
import hello.cluebackend.domain.assignment.presentation.dto.request.AssignmentCreateRequestDto;
import hello.cluebackend.domain.assignment.presentation.dto.response.GetAssignmentResponseDto;
import hello.cluebackend.domain.assignment.presentation.dto.response.StudentAssignmentRemain;
import hello.cluebackend.domain.assignment.service.AssignmentService;
import hello.cluebackend.domain.assignment.service.FileService;
import hello.cluebackend.global.config.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assignments")
public class AssignmentController {
  private final AssignmentService assignmentService;
  private final JWTUtil jWTUtil;
  private final FileService fileService;

  private Long jwtTokenTaker(HttpServletRequest request){
    String token = jWTUtil.getToken(request);
    Long userId = jWTUtil.getUserId(token);
    return userId;
  }

  // 교실 전체 과제 조회하기
  @GetMapping("/{classId}")
  public ResponseEntity<List<GetAssignmentResponseDto>> getAllAssignment(
          HttpServletRequest request,
          @PathVariable Long classId
  ){
    Long userId = jwtTokenTaker(request);
    return ResponseEntity.ok(assignmentService.getAllAssignment(classId, userId));
  }

  // 과제 생성하기
  @PostMapping("/{classId}")
  public ResponseEntity<?> createAssignment(
          HttpServletRequest request,
          @PathVariable Long classId,
          @RequestPart("metadata") AssignmentCreateRequestDto requestDto,
          @RequestPart("files") List<MultipartFile> files
  ){
    try {
      Long userId = jwtTokenTaker(request);
      assignmentService.createAssignment(userId, classId, requestDto, files);
      return ResponseEntity.status(HttpStatus.CREATED).body("과제 생성 완료");
    } catch(Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // 선생님 첨부 파일 다운 받기
  @GetMapping("/attachment/{attachmentId}")
  public ResponseEntity<Resource> downloadAttachment(
          @PathVariable Long attachmentId,
          HttpServletRequest request
  ){
    Long userId = jwtTokenTaker(request);

    try {
      Resource file = fileService.downloadFile(attachmentId, userId);
      AssignmentAttachmentDto assignmentDto = assignmentService.findById(attachmentId);
      String fullPath = assignmentDto.getFilePath();
      UrlResource resource = new UrlResource("file:" + fullPath);

      String encodedFileName = UriUtils.encode(fullPath.split("_")[1], StandardCharsets.UTF_8);
      String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";

      return ResponseEntity.ok()
              .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
              .body(resource);
    } catch(Exception e) {
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }
//  // 선생님 첨부 파일 다운 받기
//  @GetMapping("/attachment/{attachmentId}")
//  public ResponseEntity<Resource> downloadAttachment(
//          @PathVariable Long attachmentId,
//          HttpServletRequest request
//  ){
//    Long userId = jwtTokenTaker(request);
//
//    try {
//      Resource file = fileService.downloadFile(attachmentId, userId);
//
//      return ResponseEntity.ok()
//              .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"downloaded-file\"")
//              .contentType(MediaType.APPLICATION_OCTET_STREAM)
//              .body(file);
//    } catch(Exception e) {
//      log.error(e.getMessage());
//      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//    }
//  }


  // 학생 메인 페이지 과제 남은 일수
  @GetMapping("/check")
  public ResponseEntity<List<StudentAssignmentRemain>> getStudentRemainCard (
          HttpServletRequest request
  ){
    Long userId = jwtTokenTaker(request);
    List<StudentAssignmentRemain> remains = assignmentService.getUnsubmittedAssignments(userId);
    return ResponseEntity.ok(remains);
  }



//
//  @DeleteMapping("/{classId}")
//  public ResponseEntity<Void> deleteAssignment(
//          HttpServletRequest request,
//          @PathVariable Long classId,
//          @RequestParam("assignmentId") Long assignmentId
//  ) {
//    String token = jWTUtil.getToken(request);
//    Long userId = jWTUtil.getUserId(token);
//
//    try {
//      assignmentService.deleteAssignment(classId, assignmentId, userId);
//      return ResponseEntity.noContent().build();
//    } catch (AssignmentNotFoundException e) {
//      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//    } catch (UnauthorizedException e) {
//      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//    }
//  }
//
//  @GetMapping("/{classId}")
//  public ResponseEntity<?> getAssignmentList(
//          HttpServletRequest request,
//          @PathVariable Long classId
//  ) {
//    String token = jWTUtil.getToken(request);
//    Long userId = jWTUtil.getUserId(token);
//
//    return ResponseEntity.ok(assignmentService.getAssignmentList(classId));
//  }


//  @GetMapping("{classId}")
//  public ResponseEntity<List<AssignmentCardDto>> getAllAssignments(
//          @PathVariable Long classId,
//          @RequestParam Long userId
//  ){
//    return ResponseEntity.ok(assignmentService.getAssignmentsForStudent(classId, userId));
//  }
}

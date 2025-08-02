package hello.cluebackend.domain.assignmentAttachment.presentation;

import hello.cluebackend.domain.assignmentAttachment.presentation.dto.request.AssignmentUploadFileDto;
import hello.cluebackend.domain.assignmentAttachment.service.AssignmentAttachmentService;
import hello.cluebackend.global.config.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assignment/attachment")
@AllArgsConstructor
public class AssignmentAttachmentController {
  private final AssignmentAttachmentService assignmentAttachmentService;
  private final JWTUtil jwtUtil;

  // 다수의 링크 업로드 API


  // 다수의 파일 업로드 API
  @PostMapping("/upload/{assignmentId}")
  public ResponseEntity<?> uploadFiles(
          HttpServletRequest request,
          @PathVariable Long assignmentId,
          @Valid @RequestPart(value = "metadata", required = false) AssignmentUploadFileDto requestDto,
          @RequestPart(value = "files", required = false) List<MultipartFile> files
  ){
    try{
      String token = jwtUtil.getToken(request);
      Long userId = jwtUtil.getUserId(token);
      assignmentAttachmentService.uploadFile(userId,assignmentId,requestDto, files);
      return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","파일이 성공적으로 업로드 되었습니다."));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message","파일 업로드중 오류가 발생했습니다."));
    }
  }
}

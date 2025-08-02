package hello.cluebackend.domain.file.presentation;

import hello.cluebackend.domain.file.service.FileService;
import hello.cluebackend.global.config.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(name = "/api/file")
public class FileController {


  private final FileService fileService;
  private final JWTUtil jWTUtil;

  private Long jwtTokenTaker(HttpServletRequest request){
    String token = jWTUtil.getToken(request);
    Long userId = jWTUtil.getUserId(token);

    return userId;
  }

  // 선생님 과제 첨부 파일 다운 받기
//  @GetMapping("/attachment/{attachmentId}")
//  public ResponseEntity<Resource> downloadAttachment(@PathVariable Long attachmentId, HttpServletRequest request){
//    Long userId = jwtTokenTaker(request);
//
//    Resource file = fileService.downloadFile(attachmentId, userId);
//
//    return ResponseEntity.ok()
//            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"downloaded-file\"")
//            .contentType(MediaType.APPLICATION_OCTET_STREAM)
//            .body(file);
//  }

  // 학생 제출 과제 관련 파일 다운 받기

}

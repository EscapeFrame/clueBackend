package hello.cluebackend.assignment.api;

import hello.cluebackend.assignment.api.dto.request.AssignmentCreateRequestDto;
import hello.cluebackend.assignment.api.dto.response.GetAllAssignment;
import hello.cluebackend.assignment.api.dto.response.GetAssignmentResponseDto;
import hello.cluebackend.assignment.application.AssignmentService;
import hello.cluebackend.global.config.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AssignmentController {
  private final JWTUtil jwtUtil;
  private final AssignmentService assignmentService;

  // TODO : 수업실 과제 페이지 과제 젠체 조회
  @GetMapping("/get/{classId}")
  public ResponseEntity<List<GetAllAssignment>> getClassroomAssignment(
          HttpServletRequest request,
          @PathVariable Long classId
  ) {
    try {
      String token = jwtUtil.getToken(request);
      Long userId = jwtUtil.getUserId(token);

      List<GetAllAssignment> assignments = assignmentService.findAllAssignment(userId, classId);
//      List<ResponseGetAllAssignmentAttachment> attachments = assignmentA

      return ResponseEntity.ok(assignments);
    } catch (Exception e){
      log.error("Error while getting assignments", e);
      return ResponseEntity.internalServerError().build();
    }
  }

  // TODO : 과제 단일 조회

  // TODO : 과제 생성
  @PostMapping("/create/{classId}")
  public ResponseEntity<?> createAssignment(
          HttpServletRequest request,
          @PathVariable Long classId,
          @Valid @RequestBody AssignmentCreateRequestDto requestDto
  ){
    try{
      String token = jwtUtil.getToken(request);
      Long userId = jwtUtil.getUserId(token);
      assignmentService.createAssignment(userId, classId, requestDto);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      log.error("Error while create assignment",e);
      return ResponseEntity.internalServerError().build();
    }
  }

  // TODO : 과제 수정
  //  @PatchMapping("/api/assignments/{assignmentId}")
  //  public ResponseEntity modifyAssignment(HttpServletRequest request, @PathVariable Long assignmentId, @RequestBody AssignmentModifyRequestDto assignmentModifyRequestDto){
  //    jwtTokenTaker(request);

  //    return ResponseEntity.ok(assignmentService.modifyAssignment(assignmentId,assignmentModifyRequestDto));
  //  }

  // TODO : 과제 삭제
  @DeleteMapping("/delete/{classId}/{assignmentId}")
  public ResponseEntity<Void> deleteAssignment(
          HttpServletRequest request,
          @PathVariable Long assignmentId,
          @PathVariable Long classId
  ){
    try{
      String token = jwtUtil.getToken(request);
      Long userId = jwtUtil.getUserId(token);
      assignmentService.deleteAssignment(userId,classId,assignmentId);
      return ResponseEntity.noContent().build();
    }catch (Exception e){
      log.error("Error while delete assignment",e);
      return ResponseEntity.internalServerError().build();
    }
  }

  // TODO : 선생님 과제 제출 여부 조회

  // TODO : 과제 첨부 파일 업로드
  //  @PostMapping("/link/{assignmentId}")
  //  public ResponseEntity<?> addLink(
  //          HttpServletRequest request,
  //          @PathVariable Long assignmentId
  //  ){
  //    try{
  //      String token = jwtUtil.getToken(request);
  //      Long userId = jwtUtil.getUserId(token);
  //      assignmentAttachmentService.addLink()
  //    }catch (Exception e){
  //      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message","링크 추가중 오류가 발생했습니다."));
  //    }
  //  }


  //  // 파일 업로드 API
  //  @PostMapping("/upload/{assignmentId}")
  //  public ResponseEntity<?> uploadFiles(
  //          HttpServletRequest request,
  //          @PathVariable Long assignmentId,
  //          @Valid @RequestPart(value = "metadata", required = false) AssignmentUploadFileDto requestDto,
  //          @RequestPart(value = "files", required = false) List<MultipartFile> files
  //  ){
  //    try{
  //      String token = jwtUtil.getToken(request);
  //      Long userId = jwtUtil.getUserId(token);
  //      assignmentAttachmentService.uploadFile(userId, assignmentId, requestDto, files);
  //      return new  ResponseEntity<>(HttpStatus.CREATED);
  //    } catch (Exception e) {
  //      e.printStackTrace();
  //      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message","파일 업로드중 오류가 발생했습니다."));
  //    }
  //  }

  // TODO : 과제 제출 파일 업로드

  // TODO : 과제 제출 파일 삭제

  // TODO : 과제 제출

  // TODO : 과제 제출 취소

  // TODO : 과제 단일 조회
  @GetMapping("/get/{classId}/{assignmentId}")
  public ResponseEntity<GetAssignmentResponseDto> getClassRoomAssignment(
          HttpServletRequest request,
          @PathVariable Long classId,
          @PathVariable Long assignmentId
  ){
    try{
      String token = jwtUtil.getToken(request);
      Long userId = jwtUtil.getUserId(token);
      return ResponseEntity.ok().build();
    }catch (Exception e) {
      log.error("Error while getting assignment",e);
      return ResponseEntity.internalServerError().build();
    }
  }

//  // 미제출 과제 전체 조회(학생)
//  @GetMapping("/")
//  public ResponseEntity<List<StudentAssignmentRemain>> getStudentRemainCard (HttpServletRequest request){
//    String token = jWTUtil.getToken(request);
//    Long userId = jWTUtil.getUserId(token);
//
//    return ResponseEntity.ok(assignmentService.getUnsubmittedAssignments(userId));
//  }
}

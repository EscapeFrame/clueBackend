package hello.cluebackend.domain.submission.application;

import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.submission.domain.Submission;
import hello.cluebackend.domain.submission.persistence.submissionAttachment.SubmissionAttachmentRepository;
import hello.cluebackend.domain.submission.persistence.SubmissionRepository;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.classroom.service.ClassRoomService;
import hello.cluebackend.domain.classroomuser.application.ClassroomUserService;
import hello.cluebackend.domain.user.domain.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubmissionQueryService {
  private final SubmissionAttachmentRepository submissionAttachmentRepository;
  private final SubmissionRepository submissionRepository;
  private final ClassroomUserService classroomUserService;
  private final ClassRoomService classRoomService;

  // 해당 교실 모든 학생에게 과제 부여
  @Transactional
  public void assignToAllStudentsInClassroom(Long classroomId, Assignment assignment){
    ClassRoom classRoom = classRoomService.findById(classroomId).toEntity();
    List<UserEntity> users = classroomUserService.findAllClassroomUser(classRoom);
    List<Submission> submissions = users.stream()
            .map(u -> new Submission(assignment, u, false, null))
            .toList();
    submissionRepository.saveAll(submissions);
  }

  // 과제 제출하기
  @Transactional
  public Submission submitSubmission(Long submissionId) {
    Submission submission = submissionRepository.findById(submissionId)
            .orElseThrow(() -> new EntityNotFoundException("해당 제출 과제를 찾을수 없습니다."));
    submission.submit();
    return submissionRepository.save(submission);
  }

  @Transactional
  public Submission cancelSubmission(Long submissionId) {
    Submission submission = submissionRepository.findById(submissionId)
            .orElseThrow(() -> new EntityNotFoundException("해당 제출 과제를 찾을수 없습니다."));
    submission.cancel();
    return submissionRepository.save(submission);
  }

  // 파일 업로드
  @Transactional
  public void fileUpload(Long submissionId, MultipartFile file) {
    Submission submission = submissionRepository.findById(submissionId)
            .orElseThrow(() -> new EntityNotFoundException("해당 제출 과제를 찾을수 없습니다."));
  }

//  @Transactional
//  public void deletefile(Long submissionAttachmentId) {
//    SubmissionAttachment submission = submissionAttachmentRepository.findById(submissionAttachmentId)
//            .orElseThrow(() -> new EntityNotFoundException("해당 첨부파일을 찾을수 없습니다."));
//  }

//  public void uploadAttachment(UploadAttachment request, MultipartFile file) {
//    String filePath = "uploads/" + file.getOriginalFilename();
    // UUID 값으로 이름 변경
//    file.transferTo(new java.io.File(filePath));
//    submissionAttachmentRepository.save()
//  }
}
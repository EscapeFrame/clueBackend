package hello.cluebackend.domain.assignment.service;

import com.zaxxer.hikari.pool.HikariProxyCallableStatement;
import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.assignment.domain.AssignmentAttachment;
import hello.cluebackend.domain.assignment.domain.AssignmentCheck;
import hello.cluebackend.domain.assignment.domain.SubmitType;
import hello.cluebackend.domain.assignment.domain.repository.AssignmentAttachmentRepository;
import hello.cluebackend.domain.assignment.domain.repository.AssignmentContentRepository;
import hello.cluebackend.domain.assignment.domain.repository.AssignmentRepository;
import hello.cluebackend.domain.assignment.domain.repository.AssignmentCheckRepository;
import hello.cluebackend.domain.assignment.presentation.dto.request.AssignmentCreateRequestDto;
import hello.cluebackend.domain.assignment.presentation.dto.response.AssignmentDuration;
import hello.cluebackend.domain.assignment.presentation.dto.response.Assignmentfile;
import hello.cluebackend.domain.assignment.presentation.dto.response.GetAssignmentResponseDto;
import hello.cluebackend.domain.assignment.presentation.dto.response.StudentAssignmentRemain;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.classroom.domain.repository.ClassRoomRepository;
import hello.cluebackend.domain.classroomuser.domain.ClassRoomUser;
import hello.cluebackend.domain.classroomuser.domain.repository.ClassRoomUserRepository;
import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.domain.user.domain.UserEntity;
import hello.cluebackend.domain.user.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentService {
  private final UserRepository userRepository;
  private final ClassRoomUserRepository classRoomUserRepository;
  private final AssignmentRepository assignmentRepository;
  private final AssignmentAttachmentRepository assignmentAttachmentRepository;
  private final AssignmentCheckRepository assignmentCheckRepository;
  private final ClassRoomRepository classRoomRepository;

  private final FileService fileService;


  private UserEntity validated(Long userId, Long classId){
    UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을수 없습니다."));
//    System.out.println("user = " + user);
    if(!Role.TEACHER.equals(user.getRole())) {
      throw new AccessDeniedException("권한이 부족한 사용자 입니다.");
    }

    List<ClassRoomUser> classRoomUsers =  classRoomUserRepository.findByUser_UserId(userId);

    boolean isMemberOfClass = classRoomUsers.stream()
            .anyMatch(classRoomUser -> classRoomUser.getClassRoom().getClassRoomId().equals(classId));

    if (!isMemberOfClass) {
      throw new AccessDeniedException("해당 수업에 속하지 않은 사용자입니다.");
    }
    return user;
  }

  private String calculateRemainingTime(LocalDateTime now, LocalDateTime dueDate) {
    Duration duration = Duration.between(now, dueDate);
    long days = duration.toDays();
    long hours = duration.minusDays(days).toHours();

    if (duration.isNegative()) {
      return "마감됨";
    }

    return days + "일 " + hours + "시간 남음";
  }

  public String generateUniqueFileName(String originalFileName) {
    String extension = "";

    int dotIndex = originalFileName.lastIndexOf(".");
    if (dotIndex > 0) {
      extension = originalFileName.substring(dotIndex);  // 확장자 포함
    }

    String uuid = UUID.randomUUID().toString();

    return uuid + extension;
  }

  public List<GetAssignmentResponseDto> getAllAssignment(Long classId, Long userId) {

    UserEntity user = validated(classId, userId);

    List<Assignment> assignments = assignmentRepository.findAllByClassRoom_ClassRoomId(classId);

    return assignments.stream().map(assignment -> {
      List<AssignmentAttachment> attachments = assignmentAttachmentRepository.findAllByAssignment(assignment);

      List<Assignmentfile> fileDtos = attachments.stream()
              .map(attachment -> new Assignmentfile(
                      attachment.getAssignmentAttachmentId(),
                      attachment.getOriginalFileName(),
                      attachment.getFileSize()
              ))
              .toList();

      String remainingTime = calculateRemainingTime(LocalDateTime.now(), assignment.getDueDate());

      return new GetAssignmentResponseDto(
              assignment.getAssignmentId(),
              assignment.getTitle(),
              assignment.getDueDate(),
              remainingTime,
              fileDtos
      );
    }).toList();
  }

  @Transactional
  public void createAssignment(Long userId, Long classId, AssignmentCreateRequestDto requestDto, List<MultipartFile> files) {
    UserEntity user = validated(userId, classId);
    ClassRoom classRoom = classRoomRepository.findById(classId)
            .orElseThrow(() -> new EntityNotFoundException("해당 반을 찾을 수 없습니다."));

    Assignment assignment = Assignment.builder()
            .classRoom(classRoom)
            .user(user)
            .title(requestDto.getTitle())
            .content(requestDto.getContent())
            .startDate(requestDto.getStartData())
            .dueDate(requestDto.getDueDate())
            .build();

    assignmentRepository.save(assignment);

    if (files != null && !files.isEmpty()) {
      for (MultipartFile file : files) {
        String storedFileName = fileService.storeFile(file);
        AssignmentAttachment attachment = AssignmentAttachment.builder()
                .assignment(assignment)
                .user(user)
                .originalFileName(file.getOriginalFilename())
                .storedFileName(storedFileName)
                .filePath("/uploads/" + storedFileName)
                .fileSize((int) file.getSize())
                .submitType(SubmitType.FILE)
                .updateDate(LocalDateTime.now())
                .build();

        assignmentAttachmentRepository.save(attachment);
      }
    }

    List<UserEntity> students = classRoomUserRepository.findAllStudentsByClassRoomId(classId);
    for (UserEntity student : students) {
      AssignmentCheck check = AssignmentCheck.builder()
              .assignment(assignment)
              .user(student)
              .isSubmitted(false)
              .submittedAt(null)
              .build();
      assignmentCheckRepository.save(check);
    }
  }

  public List<StudentAssignmentRemain> getUnsubmittedAssignments(Long userId) {
    UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("해당 학생을 찾을수 없습니다."));

    List<Assignment> assignments = assignmentCheckRepository.findUnsubmittedAssignmentsByUserId(userId);

    return assignments.stream()
            .map(a -> new StudentAssignmentRemain(
                    a.getTitle(),
                    new AssignmentDuration(a.getStartDate(), a.getDueDate()).toString(),
                    a.getAssignmentId()
            )).collect(Collectors.toList());
  }

//  public void createAssignment(Long userId, Long classId, AssignmentCreateRequestDto requestDto, MultipartFile file) {
//    UserEntity user = userRepository.findById(userId)
//            .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));
//
//    if (user.getRole() != Role.TEACHER) {
//      throw new AccessDeniedException("과제를 생성할 권한이 없습니다.");
//    }
//
//    Assignment assignment = Assignment.builder()
//            .creator(user)
//            .classRoom(ClassRoom.builder().classRoomId(classId).build())
//            .title(requestDto.getTitle())
//            .content(requestDto.getContent())
//            .startDate(requestDto.getStartData())
//            .dueDate(requestDto.getDueDate())
//            .build();
//
//    assignmentRepository.save(assignment);
//
//    // 파일이 있는 경우에만 첨부 정보 저장
//    if (file != null && !file.isEmpty()) {
//      // 실제 파일 저장 처리 (ex: S3, 로컬, etc)
//      String storedFileName = generateUniqueFileName(file.getOriginalFilename());
//      String filePath = "TeacherAssignment/" + storedFileName;
//
//      // 예: S3Uploader.upload(file, storedFileName)
//      AssignmentAttachment attachment = AssignmentAttachment.builder()
//              .user(user)
//              .assignment(assignment)
//              .originalFileName(file.getOriginalFilename())
//              .storedFileName(storedFileName)
//              .filePath(filePath)
//              .fileSize((int) file.getSize())
//              .submitType(2) // 예시: 2 = 일반 업로드, 1 = 링크 저장
//              .updateDate(LocalDateTime.now())
//              .build();
//
//      assignmentAttachmentRepository.save(attachment);
//    }
//  }

//  public void deleteAssignment(Long userId, Long assignmentId, Long classRoomId) {
//    UserEntity user = userRepository.findById(userId)
//            .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));
//
//    if (user.getRole() != Role.TEACHER) {
//      throw new UnauthorizedException("삭제 권한이 없습니다.");
//    }
//
//    Assignment assignment = assignmentRepository.findById(assignmentId)
//            .orElseThrow(() -> new EntityNotFoundException("해당 과제를 찾을 수 없습니다."));
//
//    if (!assignment.getClassRoom().getClassRoomId().equals(classRoomId)) {
//      throw new IllegalArgumentException("해당 과제는 이 반에 속하지 않습니다.");
//    }
//
//    assignmentRepository.delete(assignment);
//  }
//
//
//
//  public Object getSubmissionStatus(Long classId, Long assignmentId) {
//    List<AssignmentCheck> checks = assignmentCheckRepository.findAllByAssignment_AssignmentId(assignmentId);
//
//    return checks.stream().map(check -> new AssignmentSubmitStatusDto(
//            check.getUser().getUsername(),
//            check.getIsSubmitted(),
//            check.getSubmittedAt(),
//            check.getUser().getUserId(),
//            check.getAssignment().getAssignmentId()
//    )).toList();
//  }
}
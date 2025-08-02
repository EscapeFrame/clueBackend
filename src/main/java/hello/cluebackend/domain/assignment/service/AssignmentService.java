package hello.cluebackend.domain.assignment.service;

import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.assignment.domain.repository.AssignmentRepository;
import hello.cluebackend.domain.assignment.presentation.dto.request.AssignmentCreateRequestDto;
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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentService {
  private final UserRepository userRepository;
  private final ClassRoomUserRepository classRoomUserRepository;
  private final AssignmentRepository assignmentRepository;
  private final ClassRoomRepository classRoomRepository;

  public Assignment createAssignment(Long userId, Long classId, AssignmentCreateRequestDto requestDto) {
    UserEntity user = validated(userId, classId);
    ClassRoom classRoom = classRoomRepository.findById(classId)
            .orElseThrow(() -> new EntityNotFoundException("해당 수업은 찾을수 없습니다."));

    validateAssignmentDates(requestDto.getStartDate(), requestDto.getEndDate());

    Assignment assignment = Assignment.builder()
            .classRoom(classRoom)
            .user(user)
            .title(requestDto.getTitle())
            .content(requestDto.getContent())
            .startDate(requestDto.getStartDate())
            .endDate(requestDto.getEndDate())
            .build();
    assignmentRepository.save(assignment);

    return assignment;

//    List<UserEntity> students = classRoomUserRepository.findAllStudentsByClassRoomId(classId);
//    submissionService.createSubmissionsForStudents(assignment, students);
  }

//
//  public List<AssignmentSubmissionStatusDto> checkAssignments(Long assignmentId) {
//    Optional<Assignment> assignment = assignmentRepository.findById(assignmentId);
//    List<Submission> assignmentChecks = assignmentCheckRepository.findAllByAssignment(assignment.orElse(null));
//
//    List<AssignmentSubmissionStatusDto> dtoList = null;
//
//    for (Submission assignmentCheck : assignmentChecks) {
//      UserEntity user = assignmentCheck.getUser();
//
//      AssignmentSubmissionStatusDto dto = new AssignmentSubmissionStatusDto(
//              user.getClassCode(), // 학번
//              user.getUsername(), // 학생 이름
//              assignmentCheck.getIsSubmitted(), // 제출 여부
//              assignmentCheck.getSubmittedAt(), // 제출 날짜
//              user.getUserId(), // 유저 아이디
//              assignment.get().getAssignmentId() // 과제 아아디
//      );
//      dtoList.add(dto);
//    }
//    return dtoList;
//  }
//
//  public String generateUniqueFileName(String originalFileName) {
//    String extension = "";
//
//    int dotIndex = originalFileName.lastIndexOf(".");
//    if (dotIndex > 0) {
//      extension = originalFileName.substring(dotIndex);  // 확장자 포함
//    }
//
//    String uuid = UUID.randomUUID().toString();
//
//    return uuid + extension;
//  }
//
//  public List<GetAssignmentResponseDto> getAllAssignment(Long classId, Long userId) {
//
//    UserEntity user = validated(classId, userId);
//
//    List<Assignment> assignments = assignmentRepository.findAllByClassRoom_ClassRoomId(classId);
//
//    return assignments.stream().map(assignment -> {
//      List<AssignmentAttachment> attachments = assignmentAttachmentRepository.findAllByAssignment(assignment);
//
//      List<Assignmentfile> fileDtos = attachments.stream()
//              .map(attachment -> new Assignmentfile(
//                      attachment.getAssignmentAttachmentId(),
//                      attachment.getOriginalFileName(),
//                      attachment.getFileSize()
//              ))
//              .toList();
//
//      String remainingTime = calculateRemainingTime(LocalDateTime.now(), assignment.getDueDate());
//
//      return new GetAssignmentResponseDto(
//              assignment.getAssignmentId(),
//              assignment.getTitle(),
//              assignment.getDueDate(),
//              remainingTime,
//              fileDtos
//      );
//    }).toList();
//  }

//  public List<StudentAssignmentRemain> getUnsubmittedAssignments(Long userId) {
//    UserEntity user = userRepository.findById(userId)
//            .orElseThrow(() -> new EntityNotFoundException("해당 학생을 찾을수 없습니다."));
//
//    List<Assignment> assignments = assignmentCheckRepository.findUnsubmittedAssignmentsByUserId(userId);

//    return assignments.stream()
//            .filter(assignment -> assignmentCheckRepository.getAssignmentCheckByAssignmentAndUser((assignment,user).)
//            .map(a -> new StudentAssignmentRemain(
//                    a.getAssignmentId(),
//                    a.getTitle(),
//                    a.getDueDate(),
//                    new AssignmentDuration(a.getStartDate(), a.getDueDate()).toString(),
//
//
//                    assignmentCheck.getIsSubmitted(),
//
//                    a.getContent(),
//
//            )).collect(Collectors.toList());
//  }

//  public AssignmentSubmissionDto checkAssignment(Long assignmentId, Long userId) {
//    UserEntity user = userRepository.findById(userId)
//            .orElseThrow(() -> new EntityNotFoundException("해당 학생을 찾을 수 없습니다."));
//
//    Submission assignmentCheck = (Submission) Optional.ofNullable(
//            assignmentCheckRepository.findUnsubmittedAssignmentByUserIdAndAssignmentId(assignmentId, userId)
//    ).orElseThrow(() -> new EntityNotFoundException("제출 정보가 존재하지 않습니다."));
//
//    List<hello.cluebackend.domain.submissionFile.domain.SubmissionFile> contents = assignmentContentRepository.findAllByUserId(userId);
//
//    List<Long> contentIds = contents.stream()
//            .map(hello.cluebackend.domain.submissionFile.domain.SubmissionFile::getAssignmentContentId)
//            .collect(Collectors.toList());
//
//    return new AssignmentSubmissionDto(
//                userId,
//                user.getClassCode(),
//                user.getUsername(),
//            assignmentCheck.getIsSubmitted(),
//            assignmentCheck.getSubmittedAt(),
//                contentIds
//        );
//  }

  private UserEntity validated(Long userId, Long classId){
    UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을수 없습니다."));

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

  private void validateAssignmentDates(LocalDateTime start, LocalDateTime end) {
    if (end.isBefore(start)) {
      throw new IllegalArgumentException("마감일은 시작일보다 앞설 수 없습니다.");
    }
  }

  private String calculateRemainingTime(LocalDateTime now, LocalDateTime dueDate) {
    Duration duration = Duration.between(now, dueDate);
    long days = duration.toDays();
    long hours = duration.minusDays(days).toHours();

    if (duration.isNegative()) {
      return "마감됨";
    }

    return days + "d" + hours + "h";
  }

}
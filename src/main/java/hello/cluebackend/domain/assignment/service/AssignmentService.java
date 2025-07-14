package hello.cluebackend.domain.assignment.service;

import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.assignment.domain.AssignmentAttachment;
import hello.cluebackend.domain.assignment.domain.AssignmentCheck;
import hello.cluebackend.domain.assignment.domain.repository.AssignmentAttachmentRepository;
import hello.cluebackend.domain.assignment.domain.repository.AssignmentCheckRepository;
import hello.cluebackend.domain.assignment.domain.repository.AssignmentRepository;
import hello.cluebackend.domain.assignment.exception.UnauthorizedException;
import hello.cluebackend.domain.assignment.presentation.dto.request.AssignmentCreateRequestDto;
import hello.cluebackend.domain.assignment.presentation.dto.request.AssignmentSubmitStatusDto;
import hello.cluebackend.domain.assignment.presentation.dto.response.AssignmentListResponseDto;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.domain.user.domain.UserEntity;
import hello.cluebackend.domain.user.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssignmentService {
  private final AssignmentCheckRepository assignmentCheckRepository;
  private final AssignmentRepository assignmentRepository;
  private final UserRepository userRepository;
  private final AssignmentAttachmentRepository assignmentAttachmentRepository;


  public String generateUniqueFileName(String originalFileName) {
    String extension = "";

    int dotIndex = originalFileName.lastIndexOf(".");
    if (dotIndex > 0) {
      extension = originalFileName.substring(dotIndex);  // 확장자 포함
    }

    String uuid = UUID.randomUUID().toString();

    return uuid + extension;
  }

  public void createAssignment(Long userId, Long classId, AssignmentCreateRequestDto requestDto, MultipartFile file) {
    UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

    if (user.getRole() != Role.TEACHER) {
      throw new AccessDeniedException("과제를 생성할 권한이 없습니다.");
    }

    Assignment assignment = Assignment.builder()
            .creator(user)
            .classRoom(ClassRoom.builder().classRoomId(classId).build())
            .title(requestDto.getTitle())
            .content(requestDto.getContent())
            .startDate(requestDto.getStartData())
            .dueDate(requestDto.getDueDate())
            .build();

    assignmentRepository.save(assignment);

    // 파일이 있는 경우에만 첨부 정보 저장
    if (file != null && !file.isEmpty()) {
      // 실제 파일 저장 처리 (ex: S3, 로컬, etc)
      String storedFileName = generateUniqueFileName(file.getOriginalFilename());
      String filePath = "TeacherAssignment/" + storedFileName;

      // 예: S3Uploader.upload(file, storedFileName)
      AssignmentAttachment attachment = AssignmentAttachment.builder()
              .user(user)
              .assignment(assignment)
              .originalFileName(file.getOriginalFilename())
              .storedFileName(storedFileName)
              .filePath(filePath)
              .fileSize((int) file.getSize())
              .submitType(2) // 예시: 2 = 일반 업로드, 1 = 링크 저장
              .updateDate(LocalDateTime.now())
              .build();

      assignmentAttachmentRepository.save(attachment);
    }
  }

  public void deleteAssignment(Long userId, Long assignmentId, Long classRoomId) {
    UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

    if (user.getRole() != Role.TEACHER) {
      throw new UnauthorizedException("삭제 권한이 없습니다.");
    }

    Assignment assignment = assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new EntityNotFoundException("해당 과제를 찾을 수 없습니다."));

    if (!assignment.getClassRoom().getClassRoomId().equals(classRoomId)) {
      throw new IllegalArgumentException("해당 과제는 이 반에 속하지 않습니다.");
    }

    assignmentRepository.delete(assignment);
  }


  public Object getAssignmentList(Long classId) {
    List<Assignment> assignments = assignmentRepository.findAllByClassRoom_ClassRoomId(classId);

    return assignments.stream().map(assignment -> {
      AssignmentAttachment attachment = assignmentAttachmentRepository.findByAssignment(assignment)
              .orElse(null);

      return new AssignmentListResponseDto(
              assignment.getTitle(),
              assignment.getContent(),
              assignment.getStartDate(),
              assignment.getDueDate(),
              attachment != null ? attachment.getOriginalFileName() : null,
              attachment != null ? attachment.getAssignmentAttachmentId() : null
      );
    }).toList();
  }

  public Object getSubmissionStatus(Long classId, Long assignmentId) {
    List<AssignmentCheck> checks = assignmentCheckRepository.findAllByAssignment_AssignmentId(assignmentId);

    return checks.stream().map(check -> new AssignmentSubmitStatusDto(
            check.getUser().getUsername(),
            check.getIsSubmitted(),
            check.getSubmittedAt(),
            check.getUser().getUserId(),
            check.getAssignment().getAssignmentId()
    )).toList();
  }
}
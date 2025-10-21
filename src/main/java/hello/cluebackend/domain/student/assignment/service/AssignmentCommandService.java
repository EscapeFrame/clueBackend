package hello.cluebackend.domain.student.assignment.service;

import hello.cluebackend.domain.student.assignment.exception.AccessDeniedException;
import hello.cluebackend.domain.student.assignment.controller.dto.response.AssignmentResponseDto;
import hello.cluebackend.domain.student.assignment.controller.dto.response.GetAllAssignmentDto;
import hello.cluebackend.domain.student.assignment.domain.Assignment;
import hello.cluebackend.domain.student.assignment.domain.AssignmentAttachment;
import hello.cluebackend.domain.student.assignment.domain.repository.AssignmentRepository;
import hello.cluebackend.domain.student.assignment.domain.repository.AssignmentAttachmentRepository;
import hello.cluebackend.domain.student.classroom.service.ClassRoomService;
import hello.cluebackend.domain.student.classroom.domain.ClassRoom;
import hello.cluebackend.domain.student.file.service.FileService;
import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.domain.user.domain.UserEntity;
import hello.cluebackend.domain.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssignmentCommandService{
  private final AssignmentRepository assignmentRepository;
  private final AssignmentAttachmentRepository assignmentAttachmentRepository;
  private final ClassRoomService classRoomService;
  private final FileService fileService;
  private final UserService userService;

  // 과제 단일 조회
  public AssignmentResponseDto findById(UUID assignmentId) {
    Assignment a = findByIdOrThrow(assignmentId);
    List<AssignmentAttachment> assignmentAttachments = assignmentAttachmentRepository.findAllByAssignment(a);

    return AssignmentResponseDto.from(a, assignmentAttachments);
  }

  // 과제 전체 조회
  public List<AssignmentResponseDto> findAllById(UUID userId, UUID classId) {
    ClassRoom classRoom = classRoomService.findById(classId).toEntity();
    UserEntity user = userService.findById(userId).toEntity();

    if(!(user.getRole() == Role.TEACHER)) {
      throw new AccessDeniedException("해당 사용자의 권한이 존재 하지 않습니다.");
    }

    List<Assignment> assignments = assignmentRepository.findAllByClassRoom(classRoom);
    return assignments.stream()
            .map(a -> findById(a.getAssignmentId()))
            .toList();
  }

  // 사용자가 속한 모든 수업 과제 조회
  public List<GetAllAssignmentDto> findAllAssignmentMe(UUID userId) {
    List<Assignment> assignments = assignmentRepository.getAllByUser(userId);
    return assignments.stream()
            .map(a -> GetAllAssignmentDto.from(a))
            .toList();
  }

  public Resource downloadAttachment(AssignmentAttachment assignmentAttachment) throws IOException {
    String path = assignmentAttachment.getValue();
    return fileService.downloadFile(path);
  }

  // 과제 ID를 통한 조회
  public Assignment findByIdOrThrow(UUID assignmentId) {
    return assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new EntityNotFoundException("해당 과제를 찾을수 없습니다."));
  }

  public AssignmentAttachment findAssignmentAttachmentByIdOrderThrow(UUID attachmentId){
    return assignmentAttachmentRepository.findById(attachmentId)
            .orElseThrow(() -> new EntityNotFoundException("해당 첨부 파일을 찾을수 없습니다."));
  }
}

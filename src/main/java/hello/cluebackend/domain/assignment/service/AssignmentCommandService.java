package hello.cluebackend.domain.assignment.service;

import hello.cluebackend.domain.assignment.controller.dto.response.AssignmentResponseDto;
import hello.cluebackend.domain.assignment.controller.dto.response.GetAllAssignmentDto;
import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.assignment.domain.AssignmentAttachment;
import hello.cluebackend.domain.assignment.domain.repository.AssignmentAttachmentRepository;
import hello.cluebackend.domain.assignment.domain.repository.AssignmentRepository;
import hello.cluebackend.domain.assignment.exception.AccessDeniedException;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.classroom.service.ClassRoomService;
import hello.cluebackend.domain.file.service.FileService;
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
public class AssignmentCommandService {
  private final UserService userService;
  private final ClassRoomService classRoomService;
  private final FileService fileService;
  private final AssignmentAttachmentRepository assignmentAttachmentRepository;
  private final AssignmentRepository assignmentRepository;

  public AssignmentAttachment findAssignmentAttachmentByIdOrderThrow(UUID userId, UUID attachmentId){
    UserEntity user = userService.findById(userId).toEntity();

    return assignmentAttachmentRepository.findById(attachmentId)
            .orElseThrow(() -> new EntityNotFoundException("해당 첨부 파일을 찾을수 없습니다."));
  }

  // 과제 단일 조회
  public AssignmentResponseDto findById(UUID assignmentId) {
    Assignment a = findByIdOrThrow(assignmentId);
    List<AssignmentAttachment> assignmentAttachments = assignmentAttachmentRepository.findAllByAssignment(a);

    return AssignmentResponseDto.from(a, assignmentAttachments);
  }

  // 사용자가 속한 모든 수업 과제 조회
  public List<GetAllAssignmentDto> findAllAssignmentMe(UUID userId) {
    List<Assignment> assignments = assignmentRepository.getAllByUser(userId);
    return assignments.stream()
            .map(a -> GetAllAssignmentDto.from(a))
            .toList();
  }

  // 과제 전체 조회
  public List<AssignmentResponseDto> findAllById(UUID userId, UUID classId) {
    ClassRoom classRoom = classRoomService.findById(userId, classId).toEntity();
    UserEntity user = userService.findById(userId).toEntity();

    if(!(user.getRole() == Role.TEACHER)) {
      throw new AccessDeniedException("해당 사용자의 권한이 존재 하지 않습니다.");
    }

    List<Assignment> assignments = assignmentRepository.findAllByClassRoom(classRoom);
    return assignments.stream()
            .map(a -> findById(a.getAssignmentId()))
            .toList();
  }

  // 과제 ID를 통한 조회
  public Assignment findByIdOrThrow(UUID assignmentId) {
    return assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new EntityNotFoundException("해당 과제를 찾을수 없습니다."));
  }

  public Resource downloadAttachment(AssignmentAttachment assignmentAttachment) throws IOException {
    String path = assignmentAttachment.getValue();
    return fileService.downloadFile(path);
  }
}

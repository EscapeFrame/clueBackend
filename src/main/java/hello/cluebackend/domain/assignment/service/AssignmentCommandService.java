package hello.cluebackend.domain.assignment.service;

import hello.cluebackend.application.assignment.dto.response.AssignmentAttachmentDto;
import hello.cluebackend.application.assignment.dto.response.AssignmentDto;
import hello.cluebackend.application.assignment.dto.response.GetAllAssignmentDto;
import hello.cluebackend.application.classroom.mapper.ClassRoomMapper;
import hello.cluebackend.domain.assignment.model.Assignment;
import hello.cluebackend.domain.assignment.model.AssignmentAttachment;
import hello.cluebackend.infrastructure.persistence.assignmentattachment.AssignmentAttachmentJpaRepository;
import hello.cluebackend.infrastructure.persistence.assignment.AssignmentJpaRepository;
import hello.cluebackend.domain.assignment.exception.AccessDeniedException;
import hello.cluebackend.domain.classroom.model.ClassRoom;
import hello.cluebackend.domain.classroom.service.ClassRoomQueryService;
import hello.cluebackend.domain.file.service.FileService;
import hello.cluebackend.domain.user.model.Role;
import hello.cluebackend.domain.user.model.UserEntity;
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
  private final ClassRoomQueryService classRoomQueryService;
  private final FileService fileService;
  private final AssignmentAttachmentJpaRepository assignmentAttachmentJpaRepository;
  private final AssignmentJpaRepository assignmentJpaRepository;
  private final ClassRoomMapper classRoomMapper;

  public AssignmentAttachment findAssignmentAttachmentByIdOrderThrow(UUID userId, UUID attachmentId){
    UserEntity user = userService.findById(userId);

    return assignmentAttachmentJpaRepository.findById(attachmentId)
            .orElseThrow(() -> new EntityNotFoundException("해당 첨부 파일을 찾을수 없습니다."));
  }

  // 과제 단일 조회
  public AssignmentDto findById(UUID assignmentId) {
    Assignment a = findByIdOrThrow(assignmentId);
    List<AssignmentAttachment> assignmentAttachments = assignmentAttachmentJpaRepository.findAllByAssignment(a);

    return AssignmentDto.from(a, assignmentAttachments);
  }

  // 사용자가 속한 모든 수업 과제 조회
  public List<GetAllAssignmentDto> findAllAssignmentMe(UUID userId) {
    List<Assignment> assignments = assignmentJpaRepository.getAllByUser(userId);
    return assignments.stream()
            .map(a -> GetAllAssignmentDto.from(a))
            .toList();
  }

  // 과제 전체 조회
  public List<AssignmentDto> findAllById(UUID userId, UUID classId) {
    ClassRoom classRoom = classRoomMapper.fromClassRoomDtoToEntity(classRoomQueryService.findById(userId, classId));
    UserEntity user = userService.findById(userId);

    if(!(user.getRole() == Role.TEACHER)) {
      throw new AccessDeniedException("해당 사용자의 권한이 존재 하지 않습니다.");
    }

    List<Assignment> assignments = assignmentJpaRepository.findAllByClassRoom(classRoom);
    return assignments.stream()
            .map(a -> findById(a.getAssignmentId()))
            .toList();
  }

  // 과제 ID를 통한 조회
  public Assignment findByIdOrThrow(UUID assignmentId) {
    return assignmentJpaRepository.findById(assignmentId)
            .orElseThrow(() -> new EntityNotFoundException("해당 과제를 찾을수 없습니다."));
  }

  public Resource downloadAttachment(AssignmentAttachment assignmentAttachment) throws IOException {
    String path = assignmentAttachment.getValue();
    return fileService.downloadFile(path);
  }

  // 과제 첨부 파일 목록 조회
  public List<AssignmentAttachmentDto> findAttachmentsByAssignmentId(UUID assignmentId) {
    Assignment assignment = findByIdOrThrow(assignmentId);
    List<AssignmentAttachment> attachments = assignmentAttachmentJpaRepository.findAllByAssignment(assignment);
    return attachments.stream()
            .map(AssignmentAttachmentDto::from)
            .toList();
  }
}

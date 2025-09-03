package hello.cluebackend.domain.assignment.application;

import hello.cluebackend.domain.assignment.api.dto.response.AssignmentAttachmentDto;
import hello.cluebackend.domain.assignment.api.dto.response.AssignmentResponseDto;
import hello.cluebackend.domain.assignment.api.dto.response.GetAllAssignmentDto;
import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.assignment.domain.AssignmentAttachment;
import hello.cluebackend.domain.assignment.persistence.AssignmentRepository;
import hello.cluebackend.domain.assignment.persistence.AssignmentAttachmentRepository;
import hello.cluebackend.domain.classroom.service.ClassRoomService;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.file.service.FileService;
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
  private final ClassRoomService classRoomService;
  private final AssignmentAttachmentRepository assignmentAttachmentRepository;
  private final FileService fileService;

  // 과제 단일 조회
  public AssignmentResponseDto findById(UUID assignmentId) {
    Assignment a = findByIdOrThrow(assignmentId);
    List<AssignmentAttachment> assignmentAttachments = assignmentAttachmentRepository.findAllByAssignment(a);

    List<AssignmentAttachmentDto> assignmentAttachmentDtos = assignmentAttachments.stream()
            .map(aa -> AssignmentAttachmentDto.builder()
                    .type(aa.getType())
                    .value(aa.getValue())
                    .originalFileName(aa.getOriginalFileName())
                    .contentType(aa.getContentType())
                    .size(aa.getSize())
                    .build())
            .toList();

    return AssignmentResponseDto.builder()
            .assignmentId(a.getAssignmentId())
            .title(a.getTitle())
            .content(a.getContent())
            .startDate(a.getStartDate())
            .endDate(a.getEndDate())
            .userName(a.getUser().getUsername())
            .xAssignmentResponseDtos(assignmentAttachmentDtos)
            .build();
  }

  // 과제 전체 조회
  public List<AssignmentResponseDto> findAllById(UUID userId, UUID classId) {
    ClassRoom classRoom = classRoomService.findById(classId).toEntity();
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

  public AssignmentAttachment findAssignmentAttachmentByIdOrderThrow(UUID attachmentId){
    return assignmentAttachmentRepository.findById(attachmentId)
            .orElseThrow(() -> new EntityNotFoundException("해당 첨부 파일을 찾을수 없습니다."));
  }

  // 사용자가 속한 모든 수업 과제 조회
  public List<GetAllAssignmentDto> findAllAssignmentMe(UUID userId) {
    List<Assignment> assignments = assignmentRepository.getAllByUser(userId);
    return assignments.stream()
            .map(a -> GetAllAssignmentDto.builder()
                    .assignmentId(a.getAssignmentId())
                    .title(a.getTitle())
                    .startDate(a.getStartDate())
                    .endDate(a.getEndDate())
                    .build()
            )
            .toList();
  }

  public Resource downloadAttachment(AssignmentAttachment assignmentAttachment) throws IOException {
    String path = assignmentAttachment.getValue();
    return fileService.downloadFile(path);
  }
}

package hello.cluebackend.domain.assignment.service;

import hello.cluebackend.application.assignment.dto.request.AssignmentAttachmentDto;
import hello.cluebackend.application.assignment.dto.request.CreateAssignmentDto;
import hello.cluebackend.application.assignment.dto.request.ModifyAssignmentDto;
import hello.cluebackend.domain.assignment.model.Assignment;
import hello.cluebackend.domain.assignment.model.AssignmentAttachment;
import hello.cluebackend.domain.assignment.model.FileType;
import hello.cluebackend.infrastructure.persistence.assignmentattachment.AssignmentAttachmentJpaRepository;
import hello.cluebackend.infrastructure.persistence.assignment.AssignmentJpaRepository;
import hello.cluebackend.domain.classroom.model.ClassRoom;
import hello.cluebackend.infrastructure.persistence.classroom.ClassRoomJpaRepository;
import hello.cluebackend.domain.file.service.FileService;
import hello.cluebackend.domain.user.model.UserEntity;
import hello.cluebackend.domain.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssignmentQueryService {
  private final UserService userService;
  private final FileService fileService;
  private final AssignmentCommandService assignmentCommandService;
  private final ClassRoomJpaRepository classRoomJpaRepository;
  private final AssignmentJpaRepository assignmentJpaRepository;
  private final AssignmentAttachmentJpaRepository assignmentAttachmentJpaRepository;

  // ------------------------------- ASSIGNMENT ---------------------------------------------- //

  // 과제 생성
  @Transactional
  public Assignment save(UUID userId, CreateAssignmentDto request) {
    UserEntity user = userService.findById(userId);

    ClassRoom classRoom = classRoomJpaRepository.findById(request.classId())
            .orElseThrow(() -> new EntityNotFoundException("해당 교실을 찾을수 없습니다"));

    Assignment assignment  = Assignment.builder()
            .classRoom(classRoom)
            .user(user)
            .title(request.title())
            .content(request.content())
            .startDate(request.startDate())
            .endDate(request.endDate())
            .build();

    assignmentJpaRepository.save(assignment);
    return assignment;
  }

  // 과제 수정
  @Transactional
  public UUID patchAssignment(UUID userId, UUID assignmentId, ModifyAssignmentDto dto) {
    Assignment assignment = assignmentCommandService.findByIdOrThrow(assignmentId);
    assignment.updateDetails(dto.getTitle(), dto.getContent(), dto.getStartDate(), dto.getEndDate());
    return assignment.getAssignmentId();
  }

  // 과제 삭제
  @Transactional
  public void delete(UUID userId, UUID assignmentId) {
    Assignment assignment = assignmentCommandService.findByIdOrThrow(assignmentId);
    assignmentJpaRepository.delete(assignment);
  }

  // --------------------------------- ATTACHMENT --------------------------------------------------- //

  // 첨부 파일 추가
  public void uploadFileAttachment(UUID assignmentId, MultipartFile file) throws IOException {
    Assignment assignment = assignmentCommandService.findByIdOrThrow(assignmentId);

    String storedFileName = fileService.storeFile(file);

    AssignmentAttachment result = AssignmentAttachment.builder()
            .assignment(assignment)
            .type(FileType.FILE)
            .value(storedFileName)
            .originalFileName(file.getOriginalFilename())
            .contentType(file.getContentType())
            .size(file.getSize())
            .build();
    assignmentAttachmentJpaRepository.save(result);
  }

  // url 업로드
  @Transactional
  public void uploadUrlAttachment(UUID userId, UUID assignmentId, List<AssignmentAttachmentDto> dtos) {
    Assignment assignment = assignmentCommandService.findByIdOrThrow(assignmentId);

    List<AssignmentAttachment> result = dtos.stream()
            .map(dto -> AssignmentAttachment.builder()
                    .assignment(assignment)
                    .type(FileType.URL)
                    .value(dto.url())
                    .build())
            .toList();
    assignmentAttachmentJpaRepository.saveAll(result);
  }

  // 첨부 파일 업로드 삭제
  @Transactional
  public void deleteAttachment(UUID userId, UUID attachmentId) {
    AssignmentAttachment assignmentAttachment = assignmentCommandService.findAssignmentAttachmentByIdOrderThrow(userId, attachmentId);
    if(assignmentAttachment.getType() == FileType.FILE){
      fileService.deleteFile(assignmentAttachment.getValue());
    }
    assignmentAttachmentJpaRepository.delete(assignmentAttachment);
  }

  public Resource downloadAttachment(AssignmentAttachment assignmentAttachment) throws IOException {
    String path = assignmentAttachment.getValue();
    return fileService.downloadFile(path);
  }
}

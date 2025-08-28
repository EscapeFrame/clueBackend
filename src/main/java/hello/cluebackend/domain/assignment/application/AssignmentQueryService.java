package hello.cluebackend.domain.assignment.application;

import hello.cluebackend.domain.assignment.api.dto.request.AssignmentAttachmentDto;
import hello.cluebackend.domain.assignment.api.dto.request.CreateAssignmentDto;
import hello.cluebackend.domain.assignment.api.dto.request.ModifyAssignmentDto;
import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.assignment.domain.AssignmentAttachment;
import hello.cluebackend.domain.assignment.domain.FileType;
import hello.cluebackend.domain.assignment.persistence.AssignmentRepository;
import hello.cluebackend.domain.assignment.persistence.AssignmentAttachmentRepository;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.classroom.domain.repository.ClassRoomRepository;
import hello.cluebackend.domain.file.service.FileService;
import hello.cluebackend.domain.user.domain.UserEntity;
import hello.cluebackend.domain.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentQueryService{
  private final AssignmentRepository assignmentRepository;
  private final ClassRoomRepository classRoomRepository;
  private final AssignmentCommandService assignmentCommandService;
  private final AssignmentAttachmentRepository assignmentAttachmentRepository;
  private final FileService fileService;
  private final UserService userService;

  // 과제 생성
  @Transactional
  public Assignment save(Long userId, CreateAssignmentDto request) {
    UserEntity user = userService.findById(userId).toEntity();

    ClassRoom classRoom = classRoomRepository.findById(request.classId())
                    .orElseThrow(() -> new EntityNotFoundException("해당 교실을 찾을수 없습니다"));

    Assignment assignment  = Assignment.builder()
            .classRoom(classRoom)
            .user(user)
            .title(request.title())
            .content(request.content())
            .startDate(request.startDate())
            .endDate(request.endDate())
            .build();

    assignmentRepository.save(assignment);
    return assignment;
  }

  // 과제 삭제
  @Transactional
  public void delete(Long assignmentId) {
    Assignment assignment = assignmentCommandService.findByIdOrThrow(assignmentId);
    assignmentRepository.delete(assignment);
  }

  // 과제 수정
  @Transactional
  public Long patchAssignment(Long assignmentId, ModifyAssignmentDto dto){
    Assignment assignment = assignmentCommandService.findByIdOrThrow(assignmentId);
    assignment.patch(dto.getTitle(), dto.getContent(), dto.getStartDate(), dto.getEndDate());
    return assignment.getAssignmentId();
  }

  // url 업로드
  @Transactional
  public void uploadUrlAttachment(Long assignmentId, List<AssignmentAttachmentDto> dtos) {
    Assignment assignment = assignmentCommandService.findByIdOrThrow(assignmentId);

    List<AssignmentAttachment> result = dtos.stream()
            .map(dto -> AssignmentAttachment.builder()
                    .assignment(assignment)
                    .type(FileType.URL)
                    .value(dto.url())
                    .build())
            .toList();

    assignmentAttachmentRepository.saveAll(result);
  }

  // 첨부 파일 추가
  public void uploadFileAttachment(Long assignmentId, MultipartFile file) {
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
    assignmentAttachmentRepository.save(result);
  }

  // 첨부 파일 업로드 삭제
  @Transactional
  public void deleteAttachment(Long attachmentId) {
    AssignmentAttachment assignmentAttachment = assignmentCommandService.findAssignmentAttachmentByIdOrderThrow(attachmentId);
    if(assignmentAttachment.getType() == FileType.FILE){
      fileService.deleteFile(assignmentAttachment.getValue());
    }
    assignmentAttachmentRepository.delete(assignmentAttachment);
  }



}
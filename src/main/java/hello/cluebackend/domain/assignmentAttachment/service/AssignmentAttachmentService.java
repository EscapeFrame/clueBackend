package hello.cluebackend.domain.assignmentAttachment.service;

import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.assignment.domain.repository.AssignmentRepository;
import hello.cluebackend.domain.assignmentAttachment.domain.AttachmentDocument;
import hello.cluebackend.domain.assignmentAttachment.domain.AttachmentLink;
import hello.cluebackend.domain.assignmentAttachment.domain.repository.AssignmentAttachmentRepository;
import hello.cluebackend.domain.assignmentAttachment.presentation.dto.request.AssignmentUploadFileDto;
import hello.cluebackend.domain.file.service.FileService;
import hello.cluebackend.domain.user.domain.UserEntity;
import hello.cluebackend.domain.user.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssignmentAttachmentService {
  private UserRepository userRepository;
  private AssignmentRepository assignmentRepository;
  private AssignmentAttachmentRepository assignmentAttachmentRepository;

  private FileService fileService;

  public void uploadFile(
          Long userId,
          Long assignmentId,
          @Valid AssignmentUploadFileDto requestDto,
          List<MultipartFile> files
  ) {
    UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을수 없습니다."));

    Assignment assignment = assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new EntityNotFoundException("해당 과제를 찾을수 없습니다."));

    if (files != null && !files.isEmpty()) { // 파일 일때
      for (MultipartFile sfile : files) {
        String storedFileName = fileService.storeFile(sfile);
        AttachmentDocument document = AttachmentDocument.builder()
                .assignment(assignment)
                .user(user)
                .originalFileName(sfile.getOriginalFilename())
                .storedFileName(storedFileName)
                .filePath("/uploads/assignmentAttachment" + assignment.getClassRoom().getName() + storedFileName)
                .fileSize(sfile.getSize())
                .updateDate(LocalDateTime.now())
                .build();

        assignmentAttachmentRepository.save(document);
      }
    }

    if(requestDto.getUrls() != null && !requestDto.getUrls().isEmpty()){ // url 일떄
      for (String url : requestDto.getUrls()) {
        AttachmentLink link = AttachmentLink.builder()
                .assignment(assignment)
                .user(user)
                .updateDate(LocalDateTime.now())
                .url(url)
                .build();
        assignmentAttachmentRepository.save(link);
      }
    }
  }
}

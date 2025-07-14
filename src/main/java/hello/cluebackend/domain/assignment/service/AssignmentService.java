package hello.cluebackend.domain.assignment.service;

import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.assignment.domain.AssignmentAttachment;
import hello.cluebackend.domain.assignment.domain.AssignmentContent;
import hello.cluebackend.domain.assignment.domain.repository.AssignmentAttachmentRepository;
import hello.cluebackend.domain.assignment.domain.repository.AssignmentCheckRepository;
import hello.cluebackend.domain.assignment.domain.repository.AssignmentRepository;
import hello.cluebackend.domain.assignment.presentation.dto.request.AssignmentCreateRequestDto;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.domain.user.domain.UserEntity;
import hello.cluebackend.domain.user.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AssignmentService {
  private final AssignmentCheckRepository assignmentCheckRepository;
  private final AssignmentRepository assignmentRepository;
  private final UserRepository userRepository;
  private final AssignmentAttachmentRepository assignmentAttachmentRepository;

  public void createAssignment(Long userId, Long classId , AssignmentCreateRequestDto requestDto, MultipartFile file){
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

    AssignmentAttachment assignmentAttachment = AssignmentAttachment.builder()
            .user(user)
            .assignment(assignment)
            .originalFileName(requestDto.getOriginalFileName())
            .storedFileName(requestDto.getStoredFileName())
            .filePath(requestDto.getFilePath())
            .fileSize(requestDto.getFileSize())
            .submitType(requestDto.getSubmitType())
            .updateDate(LocalDateTime.now())
            .build();

    assignmentAttachmentRepository.save(assignmentAttachment);
  }

  public void deleteAssignment(Long userId ,Long assignmentId, Long classRoomId){
    UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));

    if(user.getRole() == Role.STUDENT){
      assignmentRepository.deleteById(assignmentId);
    }else{
      // TODO : 권한 부족
    }
  }


}
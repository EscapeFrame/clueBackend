package hello.cluebackend.domain.assignment.service;

import hello.cluebackend.domain.assignment.domain.AssignmentEntity;
import hello.cluebackend.domain.assignment.domain.repository.AssignmentRepository;
import hello.cluebackend.domain.assignment.presentation.dto.request.CreateAssignmentRequestDto;
import hello.cluebackend.domain.assignment.presentation.dto.response.AllStudentAssignmentResponseDto;
import hello.cluebackend.domain.assignment.presentation.dto.response.CreateAssignmentResponseDto;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.classroom.domain.repository.ClassRoomRepository;
import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.domain.user.domain.UserEntity;
import hello.cluebackend.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentService {
  private final AssignmentRepository assignmentRepository;
  private final UserRepository userRepository;
  private final ClassRoomRepository roomRepository;

  public List<AllStudentAssignmentResponseDto> allStudentAssignment(
          int classId,
          Authentication authentication
  ) {
    String name = authentication.getName();
    UserEntity user = userRepository.findByUsername(name)
            .orElseThrow(() -> new RuntimeException("시간 초과"));

    Role role = user.getRole();
    if((role == Role.TEACHER || role == Role.ADMIN )){
      List<AllStudentAssignmentResponseDto> response = assignmentRepository.findAllByClassRoom(classId);
      return response;
    }
    return null;
  }

  public CreateAssignmentResponseDto create(
          Long class_id,
          CreateAssignmentRequestDto request,
          Authentication authentication
  ) {
    String name = authentication.getName();
    UserEntity user = userRepository.findByUsername(name)
            .orElseThrow(() -> new RuntimeException("시간 초과"));
    ClassRoom room = roomRepository.findById(class_id)
            .orElseThrow(() -> new RuntimeException("시간 초과"));

    Role role = user.getRole();
    if (role == Role.TEACHER || role == Role.ADMIN) {
      AssignmentEntity entity = new AssignmentEntity(
              request.getTitle(),
              request.getContent(),
              request.getStartDate(),
              request.getEndDate() ,
              room,
              user
      );
      assignmentRepository.save(entity);
    }
    return null;
  }

  public void delete(
          Long assignmentId,
          Authentication authentication
  ) {
    String name = authentication.getName();
    UserEntity user = userRepository.findByUsername(name)
            .orElseThrow(() -> new RuntimeException("시간 초과"));

    Role role = user.getRole();
    if(role == Role.TEACHER || role == Role.ADMIN){
      assignmentRepository.deleteById(assignmentId);
    }
  }


  public ResponseEntity<String> spelizationGetAssignment(int classId, int assignmentId, Authentication authentication) {
    return null;
  }
}

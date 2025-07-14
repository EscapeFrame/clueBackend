package hello.cluebackend.domain.assignment.service;

import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.assignment.domain.repository.AssignmentCheckRepository;
import hello.cluebackend.domain.assignment.domain.repository.AssignmentRepository;
import hello.cluebackend.domain.assignment.presentation.dto.request.AssignmentCreateRequestDto;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.domain.user.domain.UserEntity;
import hello.cluebackend.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssignmentService {
  private final AssignmentCheckRepository assignmentCheckRepository;
  private final AssignmentRepository assignmentRepository;
  private final UserRepository userRepository;

  public void createAssignment(Long userId, Long classId ,AssignmentCreateRequestDto requestDto){
    Assignment assignment = Assignment.builder()
            .creator(UserEntity.builder().userId(userId).build())
            .classRoom(ClassRoom.builder().classRoomId(classId).build())
            .title(requestDto.getTitle())
            .content(requestDto.getContent())
            .startDate(requestDto.getStartData())
            .dueDate(requestDto.getEndDate())
            .build();
    assignmentRepository.save(assignment);
  }

  public void deleteAssignment(Long userId ,Long assignmentId, Long classRoomId){
    UserEntity user = userRepository.findById(userId);

    if(user.getRole() == Role.STUDENT){
      assignmentRepository.deleteById(assignmentId);
    }else{

    }
  }
}
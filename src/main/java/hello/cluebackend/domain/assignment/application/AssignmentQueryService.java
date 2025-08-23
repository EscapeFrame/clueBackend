package hello.cluebackend.domain.assignment.application;

import hello.cluebackend.domain.assignment.api.dto.request.CreateAssignmentDto;
import hello.cluebackend.domain.assignment.api.dto.request.ModifyAssignmentDto;
import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.assignment.persistence.AssignmentRepository;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.classroom.domain.repository.ClassRoomRepository;
import hello.cluebackend.domain.user.domain.UserEntity;
import hello.cluebackend.domain.user.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AssignmentQueryService{
  private final AssignmentRepository assignmentRepository;
  private final UserRepository userRepository;
  private final ClassRoomRepository classRoomRepository;

  // 과제 생성
  @Transactional
  public Assignment save(Long userId, CreateAssignmentDto request) {
    UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을수 없습니다."));
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
    Assignment assignment = assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new EntityNotFoundException("해당 과제를 찾을수 없습니다."));
    assignmentRepository.delete(assignment);
  }

  // 과제 수정
  @Transactional
  public Assignment patchAssignment(Long id, ModifyAssignmentDto dto){
    Assignment assignment = assignmentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("해당 과제를 찾을수 없습니다."));
    assignment.patch(dto);
    return assignment;
  }
}
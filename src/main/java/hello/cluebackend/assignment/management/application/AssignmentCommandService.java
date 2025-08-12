package hello.cluebackend.assignment.management.application;

import hello.cluebackend.assignment.management.api.dto.response.GetAllAssignmentResponse;
import hello.cluebackend.assignment.management.api.dto.response.GetAllClassRoomAssignmentResponse;
import hello.cluebackend.assignment.management.api.dto.response.GetAllSubmissionCheck;
import hello.cluebackend.assignment.management.domain.Assignment;
import hello.cluebackend.assignment.management.persistence.AssignmentRepository;
import hello.cluebackend.assignment.participation.domain.Submission;
import hello.cluebackend.assignment.participation.persistence.SubmissionRepository;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.classroom.domain.repository.ClassRoomRepository;
import hello.cluebackend.domain.classroom.service.ClassRoomService;
import hello.cluebackend.domain.user.domain.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssignmentCommandService{
  private final AssignmentRepository assignmentRepository;
  private final ClassRoomService classRoomService;
  private final ClassRoomRepository classRoomRepository;
  private final SubmissionRepository submissionRepository;

  // 해당 사용자가 속한 모든 과제 출력 (단, 마감이 된 과제는 반환하지 않는다.)
  public List<GetAllAssignmentResponse> findAllAssignment(UserEntity user) {
    List<Assignment> assignments = assignmentRepository.getAllByUser(user);
    return assignments.stream()
            .map(a -> new GetAllAssignmentResponse(a.getAssignmentId(), a.getTitle(), a.getStartDate(), a.getEndDate()))
            .toList();
  }

  // 해당 교실에 속한 모든 과제 출력( 마감이 된 과제도 반환한다.)
  public List<GetAllClassRoomAssignmentResponse> findAllClassroomAssignment(Long classId) {
    ClassRoom classRoom = classRoomRepository.findByClassRoomId(classId); // 나중에 Service 코드로 바꿔야 함.
    List<Assignment> assignments = assignmentRepository.getAllClassroomAssignmentByUser(classRoom);
    List<GetAllClassRoomAssignmentResponse> result = assignments.stream()
            .map(a -> new GetAllClassRoomAssignmentResponse(a.getAssignmentId(), false, a.getTitle(), a.getStartDate(), a.getEndDate())) // 모든 학생 제출 여부 코드 만들어야 함!
            .toList();
    return result;
  }

  //
  public List<GetAllSubmissionCheck> checkIsSubmitted(Assignment assignment) {
    List<Submission> result = submissionRepository.findByAssignment(assignment);
    return result.stream()
            .map(a -> new GetAllSubmissionCheck(a.getSubmissionId(),a.getUser().getUserId(),a.getUser().getUsername(),a.getUser().getClassCode(),a.getIsSubmitted(),a.getSubmittedAt(),a.getAssignment().getEndDate()))
            .toList();
  }

  public Assignment findById(Long assignmentId) {
    return assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new EntityNotFoundException("Entity not found : " + assignmentId));
  }
}

package hello.cluebackend.domain.assignment.management.application;

import hello.cluebackend.domain.assignment.management.api.dto.response.GetAllAssignmentResponse;
import hello.cluebackend.domain.assignment.management.api.dto.response.GetAllClassRoomAssignmentResponse;
import hello.cluebackend.domain.assignment.management.domain.Assignment;
import hello.cluebackend.domain.assignment.management.persistence.AssignmentRepository;
import hello.cluebackend.domain.assignment.participation.persistence.SubmissionRepository;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.classroom.domain.repository.ClassRoomRepository;
import hello.cluebackend.domain.classroom.service.ClassRoomService;
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

  // 사용자가 속한 모든 수업 과제 조회
  public List<GetAllAssignmentResponse> findAllAssignment(Long userId) {
    List<Assignment> assignments = assignmentRepository.getAllByUser(userId);
    return assignments.stream()
            .map(a -> new GetAllAssignmentResponse(a.getAssignmentId(), a.getTitle(), a.getStartDate(), a.getEndDate()))
            .toList();
  }

  // 해당 교실에 속한 모든 과제 출력( 마감이 된 과제도 반환한다.)
  public List<GetAllClassRoomAssignmentResponse> findAllClassroomAssignment(Long classId) {
    ClassRoom classRoom = classRoomRepository.findByClassRoomId(classId); // 나중에 Service 코드로 바꿔야 함.
    List<Assignment> assignments = assignmentRepository.getAllClassroomAssignmentByUser(classRoom);
    List<GetAllClassRoomAssignmentResponse> result = assignments.stream()
            .map(a -> new GetAllClassRoomAssignmentResponse(a.getAssignmentId(), a.getTitle(), a.getStartDate(), a.getEndDate())) // 모든 학생 제출 여부 코드 만들어야 함!
            .toList();
    return result;
  }


//  public List<GetAllSubmissionCheck> checkIsSubmitted(Assignment assignment) {
//    List<Submission> result = submissionRepository.findByAssignment(assignment);
//    return result.stream()
//            .map(a -> new GetAllSubmissionCheck(a.getSubmissionId(),a.getUser().getUserId(),a.getUser().getUsername(),a.getUser().getClassCode(),a.getIsSubmitted(),a.getSubmittedAt(),a.getAssignment().getEndDate()))
//            .toList();
//  }

//  public Assignment findById(Long assignmentId) {
//    return assignmentRepository.findById(assignmentId)
//            .orElseThrow(() -> new EntityNotFoundException("Entity not found : " + assignmentId));
//  }
}

package hello.cluebackend.domain.assignment.application;

import hello.cluebackend.domain.assignment.api.dto.response.AssignmentDto;
import hello.cluebackend.domain.assignment.api.dto.response.GetAllAssignmentDto;
import hello.cluebackend.domain.assignment.api.dto.response.GetAllClassRoomAssignmentDto;
import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.assignment.persistence.AssignmentRepository;
import hello.cluebackend.domain.submission.application.SubmissionCommandService;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
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
  private final SubmissionCommandService submissionCommandService;

  // 사용자가 속한 모든 수업 과제 조회
  public List<GetAllAssignmentDto> findAllAssignment(Long userId) {
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

  // 해당 교실에 속한 모든 과제 출력
  public List<GetAllClassRoomAssignmentDto> findAllClassroomAssignment(ClassRoom classRoom) {
    List<Assignment> assignments = assignmentRepository.findAllByClassRoom(classRoom);
    return assignments.stream()
            .map(a -> GetAllClassRoomAssignmentDto.builder()
                    .assignmentId(a.getAssignmentId())
                    .title(a.getTitle())
                    .startDate(a.getStartDate())
                    .endDate(a.getEndDate())
                    .build()
            )
            .toList();
  }

  // 과제 ID로 조회
  public AssignmentDto findById(Long assignmentId) {
     Assignment a = assignmentRepository.findById(assignmentId)
             .orElseThrow(() -> new EntityNotFoundException("Assignment not found : " + assignmentId));
     return AssignmentDto.builder()
             .assignmentId(a.getAssignmentId())
             .title(a.getTitle())
             .content(a.getContent())
             .startDate(a.getStartDate())
             .endDate(a.getEndDate())
             .build();
  }

  //
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

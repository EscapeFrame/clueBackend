package hello.cluebackend.assignment.management.application.command;

import hello.cluebackend.assignment.management.api.dto.response.GetAllAssignmentResponse;
import hello.cluebackend.assignment.management.domain.Assignment;
import hello.cluebackend.assignment.management.persistence.AssignmentRepository;
import hello.cluebackend.domain.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentManagementCommandService {
  private final AssignmentRepository assignmentRepository;

  // 해당 사용자가 속한 모든 과제 출력 (단, 마감이 된 과제는 반환하지 않는다.)
  public List<GetAllAssignmentResponse> findAllAssignment(UserEntity user) {
    List<Assignment> assignments = assignmentRepository.getAllByUser(user);

    return assignments.stream()
            .map(a -> new GetAllAssignmentResponse(a.getAssignmentId(), a.getTitle(), a.getStartDate(), a.getEndDate()))
            .toList();
  }

  // 해당 교실에 속한 모든 과제 출력( 마감이 된 과제도 반환한다.)
  public List<GetALlAssignmentResponse>
}

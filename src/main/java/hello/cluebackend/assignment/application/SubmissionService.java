package hello.cluebackend.assignment.application;

import hello.cluebackend.assignment.domain.Assignment;
import hello.cluebackend.assignment.domain.Submission;
import hello.cluebackend.assignment.persistence.SubmissionRepository;
import hello.cluebackend.domain.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmissionService {
  private final SubmissionRepository submissionRepository;


  // 학생에게 과제 할당하는 메서드
  private Submission createDefaultSubmission(Assignment assignment, UserEntity student) {
    return Submission.builder()
            .assignment(assignment)
            .user(student)
            .isSubmitted(false)
            .submittedAt(null)
            .build();
  }

  public void createSubmissionsForStudents(Assignment assignment, List<UserEntity> students) {
    List<Submission> submissions = students.stream()
            .map(user -> createDefaultSubmission(assignment, user))
            .collect(Collectors.toList());
    submissionRepository.saveAll(submissions);
  }
}

//package hello.cluebackend.domain.submission.service;
//
//import hello.cluebackend.domain.assignment.domain.Assignment;
//import hello.cluebackend.domain.submission.domain.Submission;
//import hello.cluebackend.domain.submission.domain.repository.SubmissionRepository;
//import hello.cluebackend.domain.user.domain.UserEntity;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class SubmissionService {
//  private SubmissionRepository submissionRepository;
//
//  // 학생에게 과제 할당하는 메서드
//  public void createSubmissionsForStudents(Assignment assignment, List<UserEntity> students) {
//    List<Submission> submissions = students.stream()
//            .map(student -> Submission.builder()
//                    .assignment(assignment)
//                    .user(student)
//                    .isSubmitted(false)
//                    .submittedAt(null)
//                    .build())
//            .toList();
//
//    submissionRepository.saveAll(submissions);
//  }
//
//}

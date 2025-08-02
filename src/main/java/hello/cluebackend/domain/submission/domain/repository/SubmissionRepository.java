//package hello.cluebackend.domain.submission.domain.repository;
//
//import hello.cluebackend.domain.assignment.domain.Assignment;
//import hello.cluebackend.domain.submission.domain.Submission;
//import hello.cluebackend.domain.user.domain.UserEntity;
//import io.lettuce.core.dynamic.annotation.Param;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface SubmissionRepository extends JpaRepository<Submission,Long> {
//  @Query("""
//SELECT s.assignment
//FROM Submission s
//JOIN s.assignment a
//JOIN a.classRoom cr
//JOIN cr.classRoomUserList cru
//WHERE s.user.userId = :userId
//  AND s.isSubmitted = false
//""")
//
//  List<Assignment> findUnsubmittedAssignmentsByUserId(@Param("userId") Long userId);
//  List<Submission> findAllByAssignment(Assignment assignment);
//  List<Submission> user(UserEntity user);
//  Object findUnsubmittedAssignmentByUserIdAndAssignmentId(Long assignmentId, Long userId);
//}

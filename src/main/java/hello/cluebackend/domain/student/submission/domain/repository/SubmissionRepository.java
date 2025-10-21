package hello.cluebackend.domain.student.submission.domain.repository;

import hello.cluebackend.domain.student.assignment.domain.Assignment;
import hello.cluebackend.domain.student.classroom.domain.ClassRoom;
import hello.cluebackend.domain.student.submission.domain.Submission;
import hello.cluebackend.domain.student.submission.domain.SubmissionAttachment;
import hello.cluebackend.domain.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, UUID> {
  List<Submission> findAllByAssignment(Assignment assignment);

  List<SubmissionAttachment> findAllBySubmissionId(UUID submissionId);

  List<Submission> findAllByClassRoomAndUser(ClassRoom classRoom, UserEntity user);
}

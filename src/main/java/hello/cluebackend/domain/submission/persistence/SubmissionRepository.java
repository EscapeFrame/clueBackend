package hello.cluebackend.domain.submission.persistence;

import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.submission.domain.Submission;
import hello.cluebackend.domain.submission.domain.SubmissionAttachment;
import hello.cluebackend.domain.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, UUID> {
  List<Submission> findAllByAssignment(Assignment assignment);

  List<Submission> findAllByClassRoomAndUser(ClassRoom classRoom, UserEntity user);
}

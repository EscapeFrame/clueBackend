package hello.cluebackend.infrastructure.persistence.submission;

import hello.cluebackend.domain.assignment.model.Assignment;
import hello.cluebackend.domain.classroom.model.ClassRoom;
import hello.cluebackend.domain.submission.model.Submission;
import hello.cluebackend.domain.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubmissionJpaRepository extends JpaRepository<Submission, UUID> {
  List<Submission> findAllByAssignment(Assignment assignment);

  List<Submission> findAllByClassRoomAndUser(ClassRoom classRoom, UserEntity user);

  void deleteByUser(UserEntity user);
}

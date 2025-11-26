package hello.cluebackend.infrastructure.persistence.submissionattachment;

import hello.cluebackend.domain.submission.model.Submission;
import hello.cluebackend.domain.submission.model.SubmissionAttachment;
import hello.cluebackend.domain.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubmissionAttachmentJpaRepository extends JpaRepository<SubmissionAttachment, UUID> {
  List<SubmissionAttachment> findAllBySubmission(Submission submission);

  void deleteByUser(UserEntity user);
}
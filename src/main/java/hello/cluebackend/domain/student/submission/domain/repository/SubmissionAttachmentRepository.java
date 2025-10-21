package hello.cluebackend.domain.student.submission.domain.repository;

import hello.cluebackend.domain.student.submission.domain.Submission;
import hello.cluebackend.domain.student.submission.domain.SubmissionAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubmissionAttachmentRepository extends JpaRepository<SubmissionAttachment, UUID> {
  List<SubmissionAttachment> findAllBySubmission(Submission submission);
}
package hello.cluebackend.domain.submission.persistence.submissionAttachment;

import hello.cluebackend.domain.submission.domain.SubmissionAttachment.SubmissionAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionAttachmentRepository extends JpaRepository<SubmissionAttachment, Long> {

}
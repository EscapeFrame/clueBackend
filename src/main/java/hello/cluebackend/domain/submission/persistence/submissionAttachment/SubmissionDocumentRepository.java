package hello.cluebackend.domain.submission.persistence.submissionAttachment;

import hello.cluebackend.domain.submission.domain.SubmissionAttachment.SubmissionDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionDocumentRepository extends JpaRepository<SubmissionDocument, Long> {

}

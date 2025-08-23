package hello.cluebackend.domain.submission.persistence.submissionAttachment;

import hello.cluebackend.domain.submission.domain.SubmissionAttachment.SubmissionLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionLinkRepository extends JpaRepository<SubmissionLink, Long> {

}

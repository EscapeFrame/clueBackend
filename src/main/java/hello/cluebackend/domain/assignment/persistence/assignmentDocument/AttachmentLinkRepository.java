package hello.cluebackend.domain.assignment.persistence.assignmentDocument;

import hello.cluebackend.domain.assignment.domain.AssignmentAttachment.AttachmentLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentLinkRepository extends JpaRepository<AttachmentLink, Long> {

}
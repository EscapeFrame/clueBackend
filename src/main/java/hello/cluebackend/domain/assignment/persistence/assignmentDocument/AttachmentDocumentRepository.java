package hello.cluebackend.domain.assignment.persistence.assignmentDocument;

import hello.cluebackend.domain.assignment.domain.AssignmentAttachment.AttachmentDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentDocumentRepository extends JpaRepository<AttachmentDocument, Long> {

}
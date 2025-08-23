package hello.cluebackend.domain.assignment.persistence.assignmentDocument;

import hello.cluebackend.domain.assignment.domain.AssignmentAttachment.AssignmentAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentAttachmentRepository extends JpaRepository<AssignmentAttachment,Long> {

}
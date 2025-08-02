package hello.cluebackend.domain.assignmentAttachment.domain.repository;

import hello.cluebackend.domain.assignmentAttachment.domain.AssignmentAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentAttachmentRepository extends JpaRepository<AssignmentAttachment,Long> {
}

package hello.cluebackend.assignment.management.persistence;

import hello.cluebackend.assignment.management.domain.AssignmentAttachment.AssignmentAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentAttachmentRepository extends JpaRepository<AssignmentAttachment,Long> {
}
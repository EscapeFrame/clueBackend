package hello.cluebackend.domain.assignment.domain.repository;

import hello.cluebackend.domain.assignment.domain.AssignmentAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentAttachmentRepository extends JpaRepository<Long, AssignmentAttachment> {
}

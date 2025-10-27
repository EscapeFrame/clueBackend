package hello.cluebackend.infrastructure.persistence.assignmentattachment;

import hello.cluebackend.domain.assignment.model.Assignment;
import hello.cluebackend.domain.assignment.model.AssignmentAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssignmentAttachmentJpaRepository extends JpaRepository<AssignmentAttachment, UUID> {
  List<AssignmentAttachment> findAllByAssignment(Assignment assignment);
}
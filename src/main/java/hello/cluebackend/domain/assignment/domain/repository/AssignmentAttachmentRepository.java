package hello.cluebackend.domain.assignment.domain.repository;

import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.assignment.domain.AssignmentAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssignmentAttachmentRepository extends JpaRepository<Long, AssignmentAttachment> {
  Optional<Object> findByAssignment(Assignment assignment);
}

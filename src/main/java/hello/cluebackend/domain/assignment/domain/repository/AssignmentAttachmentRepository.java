package hello.cluebackend.domain.assignment.domain.repository;

import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.assignment.domain.AssignmentAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssignmentAttachmentRepository extends JpaRepository<AssignmentAttachment, UUID> {
  List<AssignmentAttachment> findAllByAssignment(Assignment assignment);
}
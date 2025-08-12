package hello.cluebackend.assignment.management.persistence;

import hello.cluebackend.assignment.management.domain.Assignment;
import hello.cluebackend.assignment.management.domain.AssignmentAttachment.AssignmentAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentAttachmentRepository extends JpaRepository<AssignmentAttachment,Long> {
  List<AssignmentAttachment> findAllByAssignment(Assignment assignment);
}
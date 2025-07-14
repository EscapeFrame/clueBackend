package hello.cluebackend.domain.assignment.domain.repository;

import hello.cluebackend.domain.assignment.domain.AssignmentContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface assignmentContentRepository extends JpaRepository<Long, AssignmentContent> {
}

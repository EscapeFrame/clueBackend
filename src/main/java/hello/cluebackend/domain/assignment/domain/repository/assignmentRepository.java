package hello.cluebackend.domain.assignment.domain.repository;

import hello.cluebackend.domain.assignment.domain.AssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface assignmentRepository extends JpaRepository<AssignmentEntity, Long> {

}

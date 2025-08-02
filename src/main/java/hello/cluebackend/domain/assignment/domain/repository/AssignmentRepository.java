package hello.cluebackend.domain.assignment.domain.repository;

import hello.cluebackend.domain.assignment.domain.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
}

package hello.cluebackend.domain.assignment.domain.repository;

import hello.cluebackend.domain.assignment.domain.AssignmentCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentCheckRepository extends JpaRepository<AssignmentCheck, Long> {

}
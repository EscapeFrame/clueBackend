package hello.cluebackend.domain.assignment.participation.persistence;

import hello.cluebackend.domain.assignment.management.domain.Assignment;
import hello.cluebackend.domain.assignment.participation.domain.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
}

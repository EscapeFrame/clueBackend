package hello.cluebackend.assignment.participation.persistence;

import hello.cluebackend.assignment.participation.domain.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SubmissionRepository extends JpaRepository<Submission,Long> { }

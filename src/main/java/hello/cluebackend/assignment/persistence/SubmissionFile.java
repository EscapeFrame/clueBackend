package hello.cluebackend.assignment.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SubmissionFile extends JpaRepository<SubmissionFile, Long> { }
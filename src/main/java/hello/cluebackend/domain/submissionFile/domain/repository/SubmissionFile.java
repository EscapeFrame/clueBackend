package hello.cluebackend.domain.submissionFile.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionFile extends JpaRepository<SubmissionFile, Long> {
  List<SubmissionFile> findAllByUserId(Long userId);
}

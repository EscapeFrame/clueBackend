package hello.cluebackend.assignment.participation.persistence;

import hello.cluebackend.assignment.management.api.dto.response.GetSubmissionFile;
import hello.cluebackend.assignment.management.domain.Assignment;
import hello.cluebackend.assignment.participation.domain.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission,Long> {
  @Query("select s from Submission s" +
          " where s.assignment =:assignment")
  List<Submission> findByAssignment(@Param("assignment") Assignment assignment);

  @Query("select sa.SubmissionAttachmentId from SubmissionAttachment sa" +
          " join fetch sa.submission s" +
          " where s=:submission")
  GetSubmissionFile findByIdWithAttachment(@Param("submission") Submission submission);
}

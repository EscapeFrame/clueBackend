package hello.cluebackend.assignment.participation.persistence;

import hello.cluebackend.assignment.management.api.dto.response.GetSubmissionFile;
import hello.cluebackend.assignment.participation.domain.Submission;
import hello.cluebackend.assignment.participation.domain.SubmissionAttachment.SubmissionAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionAttachmentRepository extends JpaRepository<SubmissionAttachment, Long> {
  
  List<GetSubmissionFile> findBySubmissionWithAttachments(@Param("submission")Submission submission);
}
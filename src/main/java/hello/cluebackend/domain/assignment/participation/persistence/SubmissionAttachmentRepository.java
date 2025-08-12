package hello.cluebackend.domain.assignment.participation.persistence;

import hello.cluebackend.domain.assignment.management.api.dto.response.GetSubmissionFile;
import hello.cluebackend.domain.assignment.participation.domain.Submission;
import hello.cluebackend.domain.assignment.participation.domain.SubmissionAttachment.SubmissionAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionAttachmentRepository extends JpaRepository<SubmissionAttachment, Long> {

}
package hello.cluebackend.assignment.participation.persistence;

import hello.cluebackend.assignment.participation.domain.SubmissionAttachment.SubmissionAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SubmissionAttachmentRepository extends JpaRepository<SubmissionAttachment, Long> { }
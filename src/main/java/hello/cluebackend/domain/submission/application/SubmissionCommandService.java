package hello.cluebackend.domain.submission.application;

import hello.cluebackend.domain.assignment.api.dto.response.SubmissionCheck;
import hello.cluebackend.domain.assignment.application.AssignmentCommandService;
import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.assignment.persistence.AssignmentRepository;
import hello.cluebackend.domain.file.service.FileService;
import hello.cluebackend.domain.submission.api.dto.response.SubmissionAttachmentDto;
import hello.cluebackend.domain.submission.api.dto.response.SubmissionDto;
import hello.cluebackend.domain.submission.domain.Submission;
import hello.cluebackend.domain.submission.domain.SubmissionAttachment;
import hello.cluebackend.domain.submission.persistence.SubmissionRepository;
import hello.cluebackend.domain.submission.persistence.SubmissionAttachmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionCommandService {
  private final SubmissionRepository submissionRepository;
  private final SubmissionAttachmentRepository submissionAttachmentRepository;
  private final AssignmentRepository assignmentRepository;
  private final AssignmentCommandService assignmentCommandService;
  private final FileService fileService;

  // 과제 제출 여부 확인 API
  public List<SubmissionCheck> checkAssignment(Long userId, Long assignmentId) {
    Assignment assignment = assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new EntityNotFoundException("해당 과제를 찾을수 없습니다."));
    List<Submission> submissions = submissionRepository.findAllByAssignment(assignment);
    return submissions.stream()
            .filter(s -> s.getUser().getUserId().equals(userId))
            .map(s -> SubmissionCheck.builder()
                    .submissionId(s.getSubmissionId())
                    .userName(s.getUser().getUsername())
                    .classNumberGrade(s.getUser().getClassCode())
                    .isSubmitted(s.getIsSubmitted())
                    .submittedAt(s.getSubmittedAt())
                    .build()
            )
            .toList();
  }

  public List<SubmissionDto> findAllByAssignmentId(Long userId, Long assignmentId) {
    Assignment assignment = assignmentCommandService.findByIdOrThrow(assignmentId);
    List<Submission> submissions = submissionRepository.findAllByAssignment(assignment);

    return submissions.stream()
            .map(s -> SubmissionDto.builder()
                    .title(s.getAssignment().getTitle())
                    .startDate(s.getAssignment().getStartDate())
                    .endDate(s.getAssignment().getEndDate())
                    .userName(s.getUser().getUsername())
                    .isSubmitted(s.getIsSubmitted())
                    .submittedAt(s.getSubmittedAt())
                    .build()
            )
            .toList();
  }

  //
  public SubmissionDto findByAssignmentId(Long assignmentId) {
    Assignment assignment = assignmentCommandService.findByIdOrThrow(assignmentId);

    Submission s = submissionRepository.findByAssignment(assignment);

    return SubmissionDto.builder()
            .title(s.getAssignment().getTitle())
            .startDate(s.getAssignment().getStartDate())
            .endDate(s.getAssignment().getEndDate())
            .userName(s.getUser().getUsername())
            .isSubmitted(s.getIsSubmitted())
            .submittedAt(s.getSubmittedAt())
            .build();
  }

  public Submission findByIdOrThrow(Long submissionId) {
    return submissionRepository.findById(submissionId)
            .orElseThrow(() -> new EntityNotFoundException("해당 제출 과제를 찾을수 없습니다."));
  }

  public SubmissionAttachment findAssignmentAttachmentByIdOrThrow(Long submissionAttachmentId){
    return submissionAttachmentRepository.findById(submissionAttachmentId)
            .orElseThrow(() -> new EntityNotFoundException("해당 제출 과제를 찾을수 없습니다."));
  }

  public List<SubmissionAttachmentDto> findAllAssignment(Long submissionId) {
    Submission submission = findByIdOrThrow(submissionId);
    List<SubmissionAttachment> attachments = submissionAttachmentRepository.findAllBySubmission(submission);
    return attachments.stream()
            .map(sa -> SubmissionAttachmentDto.builder()
            .type(sa.getType())
            .value(sa.getValue())
            .originalFileName(sa.getOriginalFileName())
            .contentType(sa.getContentType())
            .size(sa.getSize())
            .build()
    ).toList();
  }

  public SubmissionAttachment findsubmissionAttachmentByIdOrThrow(Long submissionAttachmentId) {
    return submissionAttachmentRepository.findById(submissionAttachmentId)
            .orElseThrow(() -> new EntityNotFoundException("해당 과제 제출 첨부파일을 찾을수 없습니다."));
  }

  public Resource downloadAttachment(SubmissionAttachment submissionAttachment) throws IOException {
    String path = submissionAttachment.getValue();
    return fileService.downloadFile(path);
  }
}

//package hello.cluebackend.domain.submission.domain;
//
//import hello.cluebackend.domain.assignment.domain.Assignment;
//import hello.cluebackend.domain.submissionFile.domain.SubmissionFile;
//import hello.cluebackend.domain.user.domain.UserEntity;
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Entity
//@Table(name = "Submission")
//@Getter @Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class Submission {
//  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//  @Column(name = "submission_id")
//  private Long submissionId;
//
//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "assignment_id")
//  private Assignment assignment;
//
//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "user_id")
//  private UserEntity user;
//
//  @Column(name = "is_submitted")
//  private Boolean isSubmitted;
//
//  @Column(name = "submitted_at")
//  private LocalDateTime submittedAt;
//
//  @OneToMany
//  private List<SubmissionFile> assignmentContents;
//}

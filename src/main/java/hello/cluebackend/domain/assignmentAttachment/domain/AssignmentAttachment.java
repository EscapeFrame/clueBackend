//package hello.cluebackend.domain.assignmentAttachment.domain;
//
//import hello.cluebackend.domain.assignment.domain.Assignment;
//import hello.cluebackend.domain.user.domain.UserEntity;
//import jakarta.persistence.*;
//import lombok.*;
//import lombok.experimental.SuperBuilder;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "assignment_attachment")
//@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name = "DTYPE")
//@Getter @Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@SuperBuilder
//public abstract class AssignmentAttachment {
//  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//  @Column(name = "assignment_attachment_id")
//  private Long assignmentAttachmentId;
//
//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name="assignment_id")
//  private Assignment assignment;
//
//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "user_id")
//  private UserEntity user;
//
//  @Column(name = "update_date")
//  private LocalDateTime updateDate;
//}

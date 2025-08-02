//package hello.cluebackend.domain.assignmentAttachment.domain;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.DiscriminatorValue;
//import jakarta.persistence.Entity;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import lombok.experimental.SuperBuilder;
//
//@Entity
//@SuperBuilder
//@DiscriminatorValue("DOCUMENT")
//@Getter @Setter
//@NoArgsConstructor
//public class AttachmentDocument extends AssignmentAttachment{
//  @Column(name = "original_file_name", length = 255)
//  private String originalFileName;
//
//  @Column(name = "stored_file_name", length = 255)
//  private String storedFileName;
//
//  @Column(name = "file_path", length = 2000)
//  private String filePath;
//
//  @Column(name = "file_size")
//  private long fileSize;
//}

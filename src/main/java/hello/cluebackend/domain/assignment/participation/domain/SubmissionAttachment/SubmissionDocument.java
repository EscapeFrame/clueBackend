package hello.cluebackend.domain.assignment.participation.domain.SubmissionAttachment;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("Document")
@NoArgsConstructor
public class SubmissionDocument extends SubmissionAttachment {
  @JoinColumn(name="original_file_name")
  private String originalFileName;

  @Column(name="stored_file_name")
  private String storedFileName;

  @Column(name="file_path")
  private String filePath;

  @Column(name="file_size")
  private int fileSize;
}

package hello.cluebackend.domain.submission.domain.SubmissionAttachment;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("Link")
@NoArgsConstructor
public class SubmissionLink extends SubmissionAttachment {
  private String url;
}

package hello.cluebackend.assignment.domain.SubmissionAttachment;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("Link")
@SuperBuilder
@NoArgsConstructor
public class SubmissionLink extends SubmissionDocument {
  private String url;
}

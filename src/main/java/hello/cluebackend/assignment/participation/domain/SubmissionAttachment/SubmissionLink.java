package hello.cluebackend.assignment.participation.domain.SubmissionAttachment;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("Link")
@SuperBuilder
@NoArgsConstructor
public class SubmissionLink extends SubmissionAttachment {
  private String url;
}

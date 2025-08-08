package hello.cluebackend.assignment.management.domain.AssignmentAttachment;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@DiscriminatorValue("Link")
@Getter @Setter
@NoArgsConstructor
public class AttachmentLink extends  AssignmentAttachment{
  @Column(name = "url", length = 2000)
  private String url;
}

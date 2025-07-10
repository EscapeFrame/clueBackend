package hello.cluebackend.domain.assignment.presentation.dto.response;

import org.hibernate.sql.ast.tree.update.Assignment;

public class AssignmentStatus {
  private String title;
  private String content;
  private String endDate;

  public AssignmentStatus toEntity(){
    return Assignment.builder()
            .title(this.title)
            .content(this.content)
            .startDate(this.startDate)
            .endDate(this.endDate)
            .build();
  }
}

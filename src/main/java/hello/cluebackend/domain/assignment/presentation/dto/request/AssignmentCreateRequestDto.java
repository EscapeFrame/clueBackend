package hello.cluebackend.domain.assignment.presentation.dto.request;

import hello.cluebackend.domain.assignment.domain.SubmitType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class AssignmentCreateRequestDto {
  private String title;
  private String content;
  private LocalDateTime startData;
  private LocalDateTime dueDate;

  private List<MultipartFile> files;
}

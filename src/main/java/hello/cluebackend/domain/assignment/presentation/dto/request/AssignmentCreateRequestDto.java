package hello.cluebackend.domain.assignment.presentation.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class AssignmentCreateRequestDto {
  private String title;
  private String content;
  private LocalDateTime startData;
  private LocalDateTime dueDate;

  // 파일 저장을 위한 정보
  private String originalFileName;
  private String storedFileName;
  private String filePath;
  private int fileSize;
  private int submitType;
  private LocalDateTime updateDate;
}

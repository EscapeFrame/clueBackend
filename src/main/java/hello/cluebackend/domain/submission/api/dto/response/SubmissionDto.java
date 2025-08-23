package hello.cluebackend.domain.submission.api.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Builder
public class SubmissionDto {
  // 과제 데이터
  private String title;
  private LocalDateTime startDate;
  private LocalDateTime endDate;

  // 유저 정보
  private String userName;

  // 제출 정보
  private boolean isSubmitted;
  private LocalDateTime submittedAt;

  // 과제 정보


  // 과제 첨부파일 정보

}

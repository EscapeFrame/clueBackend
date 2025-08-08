package hello.cluebackend.assignment.management.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GetAllAssignmentResponse {
  private Long assignmentId; // 과제 아이디
  private String title; // 과제 제목
  private LocalDateTime startDate; // 과제 시작일
  private LocalDateTime endDate; // 남은 시간
}
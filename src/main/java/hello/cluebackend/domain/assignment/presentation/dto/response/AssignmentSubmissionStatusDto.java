package hello.cluebackend.domain.assignment.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record AssignmentSubmissionStatusDto(
        int studentNumber,           // 학번
        String userName,             // 학생 이름
        boolean isSubmitted,         // 제출 여부
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime submittedAt,   // 제출 날짜 (ISO-8601 String으로 변환됨)
        Long userId,                 // 학생 아이디
        Long contentId               // 과제 아이디
) {}
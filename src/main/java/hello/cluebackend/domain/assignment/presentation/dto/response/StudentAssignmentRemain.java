package hello.cluebackend.domain.assignment.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record StudentAssignmentRemain(
        Long assignmentId, // 과제 아이디
        String title, // 과제 제목
        LocalDateTime endDate, // 과제 마감일
        String remainingTime, // 마감까지 남은 시간
        Boolean IsSubmitted, // 과제 제출 여부
        String Content, // 과제 내용
        List<file> studentFiles, // 학생이 이때까지 제출한 과제
        List<file> teacherFiles // 선생님이 첨부한 파일
) {}

package hello.cluebackend.assignment.management.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assignment")
@RequiredArgsConstructor
@Slf4j
public class AssignmentManagementCommandController {
  // TODO : 메인 페이지 제작한 과제 전체 조회

  // TODO : 학습실 과제 전체 조회, 학습실 - 과제(T)

  // TODO : 특정 과제 학생 제출 여부 전체 조회, 학습실 - 과제 - 확인(T)

  // TODO : 개인 제출 과제 보기, 학습실 - 과제 - 채점(T)

}

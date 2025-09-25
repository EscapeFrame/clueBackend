package hello.cluebackend.domain.subject.application;

import hello.cluebackend.domain.subject.domain.Subject;
import hello.cluebackend.domain.subject.persistence.SubjectRepository;
import hello.cluebackend.domain.subject.presentation.dto.request.SubjectRequest;
import hello.cluebackend.domain.subject.presentation.dto.response.SubjectResponse;
import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.domain.user.domain.UserEntity;
import hello.cluebackend.domain.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubjectService {
  private final SubjectRepository subjectRepository;
  private final UserService userService;

  // 과목 학년 별로 전체 조회
  public List<SubjectResponse> findAllSubject(int grade){
    List<Subject> subjects = subjectRepository.findAllByGrade(grade);
    List<SubjectResponse> subjectRequests = subjects.stream()
            .map(subject -> SubjectResponse.from(subject))
            .toList();
    return subjectRequests;
  }

  // 과목 단일 조회
  public SubjectResponse findSubject(Long subjectId) {
    Subject subject = findByIdOrElseThrow(subjectId);
    return SubjectResponse.from(subject);
  }

  // 과목 생성
  @Transactional
  public SubjectResponse createSubject(SubjectRequest request, UUID userId) {
    UserEntity user = userService.findById(userId).toEntity();
    Subject subject = SubjectRequest.toEntity(request, user);
    subjectRepository.save(subject);
    return SubjectResponse.from(subject);
  }

  // 과목 수정
  @Transactional
  public SubjectResponse updateSubject(Long subjectId, SubjectRequest request) {
    Subject subject = findByIdOrElseThrow(subjectId);

    subject.updateDetails(request.subjectName(),request.subjectCategory(),request.subjectType(),request.weeklyHours(),request.grade());

    return SubjectResponse.from(subject);
  }

  // 과목 삭제
  @Transactional
  public void deleteSubject(Long subjectId, UUID userId) {
    if(!(userService.findById(userId).toEntity().getRole() == Role.TEACHER)){
      throw new AccessDeniedException("권한이 부족합니다.");
    }else {
      Subject subject = findByIdOrElseThrow(subjectId);
      subjectRepository.delete(subject);
    }
  }

  public Subject findByIdOrElseThrow(Long subjectId){
    return subjectRepository.findById(subjectId)
            .orElseThrow(() -> new EntityNotFoundException("해당 과목을 찾을수 없습니다."));
  }
}

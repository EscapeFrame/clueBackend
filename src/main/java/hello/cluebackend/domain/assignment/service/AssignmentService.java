package hello.cluebackend.domain.assignment.service;

import hello.cluebackend.domain.assignment.domain.AssignmentEntity;
import hello.cluebackend.domain.assignment.domain.repository.AssignmentRepository;
import hello.cluebackend.domain.assignment.presentation.dto.request.CreateAssignmentRequestDto;
import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.domain.user.domain.UserEntity;
import hello.cluebackend.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssignmentService {
  private final AssignmentRepository assignmentRepository;
  private final UserRepository userRepository;

  public String createAssignment(
          int class_id,
          CreateAssignmentRequestDto request,
          Authentication authentication
  ){
    String name = authentication.getName();
    UserEntity user = userRepository.findByUsername(name)
            .orElseThrow(() -> new RuntimeException("시간 초과"));

    Role role = user.getRole();
    if(role == Role.TEACHER || role == Role.ADMIN){

    }else{

    }

  }
}

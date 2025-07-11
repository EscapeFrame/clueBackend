package hello.cluebackend.domain.assignment.service;

import hello.cluebackend.domain.assignment.domain.repository.AssignmentRepository;
import hello.cluebackend.domain.assignment.presentation.dto.response.GetAssignment;
import hello.cluebackend.domain.user.domain.Role;
import hello.cluebackend.domain.user.domain.User;
import hello.cluebackend.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentService {
  private final AssignmentRepository assignmentRepository;
  private final UserRepository userRepository;

  public List<GetAssignment> getAllAssignments(Long classId, Long userId) {
    User user = userRepository.getById(userId);
    Role role = user.getRole();

    List<GetAssignment> response = assignmentRepository.findAllById(classId);
    return response;
  }
}

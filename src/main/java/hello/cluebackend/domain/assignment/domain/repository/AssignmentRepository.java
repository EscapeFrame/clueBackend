package hello.cluebackend.domain.assignment.domain.repository;

import hello.cluebackend.domain.assignment.domain.AssignmentEntity;
import hello.cluebackend.domain.assignment.presentation.dto.response.AllStudentAssignmentResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<AssignmentEntity, Long> {

  List<AllStudentAssignmentResponseDto> findAllByClassRoom(int classId);
}

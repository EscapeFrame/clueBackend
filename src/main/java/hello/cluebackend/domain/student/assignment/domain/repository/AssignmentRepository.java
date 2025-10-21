package hello.cluebackend.domain.student.assignment.domain.repository;

import hello.cluebackend.domain.student.assignment.domain.Assignment;
import hello.cluebackend.domain.student.classroom.domain.ClassRoom;
import hello.cluebackend.domain.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {
  @Query("SELECT a FROM Submission s" +
          " join s.assignment a" +
          " where s.user.userId =:userId" +
          " AND s.isSubmitted = false")
  List<Assignment> getAllByUser(@Param("userId") UUID userId);

  List<Assignment> findAllByClassRoom(ClassRoom classRoom);

  List<Assignment> findAllByUserAndClassRoom(UserEntity user, ClassRoom classRoom);
}
package hello.cluebackend.domain.assignment.management.persistence;

import hello.cluebackend.domain.assignment.management.domain.Assignment;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
  @Query("SELECT a FROM Submission s" +
          " join fetch s.assignment a" +
          " where s.user.userId =:userId" +
          " AND s.isSubmitted = false")
  List<Assignment> getAllByUser(@Param("userId") Long userId);

  @Query("select a from Assignment a " +
          "join fetch a.classRoom " +
          "where a.classRoom =:classRoom")
  List<Assignment> getAllClassroomAssignmentByUser(@Param("classRoom") ClassRoom classRoom);
}
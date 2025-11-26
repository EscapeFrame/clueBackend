package hello.cluebackend.infrastructure.persistence.assignment;

import hello.cluebackend.domain.assignment.model.Assignment;
import hello.cluebackend.domain.classroom.model.ClassRoom;
import hello.cluebackend.domain.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssignmentJpaRepository extends JpaRepository<Assignment, UUID> {
  @Query("SELECT a FROM Submission s" +
          " join s.assignment a" +
          " where s.user.userId =:userId" +
          " AND s.isSubmitted = false")
  List<Assignment> getAllByUser(@Param("userId") UUID userId);

  List<Assignment> findAllByClassRoom(ClassRoom classRoom);

  List<Assignment> findAllByUserAndClassRoom(UserEntity user, ClassRoom classRoom);

  void deleteByUser(UserEntity user);
}
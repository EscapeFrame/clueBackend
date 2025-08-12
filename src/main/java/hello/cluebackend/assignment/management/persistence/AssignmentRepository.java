package hello.cluebackend.assignment.management.persistence;

import hello.cluebackend.assignment.management.domain.Assignment;
import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
  @Query("select a from Assignment a " +
          "join ClassRoomUser cru ON a.classRoom = cru.classRoom " +
          "WHERE cru.user =:user")
  List<Assignment> getAllByUser(@Param("user") UserEntity user);

  @Query("select a from Assignment a " +
          "join fetch a.classRoom " +
          "where a.classRoom =:classRoom")
  List<Assignment> getAllClassroomAssignmentByUser(@Param("classRoom") ClassRoom classRoom);
}
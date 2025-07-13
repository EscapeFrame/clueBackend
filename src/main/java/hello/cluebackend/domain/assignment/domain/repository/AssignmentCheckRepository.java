package hello.cluebackend.domain.assignment.domain.repository;

import hello.cluebackend.domain.assignment.domain.AssignmentCheck;
import hello.cluebackend.domain.assignment.domain.AssignmentContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentCheckRepository extends JpaRepository<AssignmentCheck, Long> {
  @Query("""
        SELECT ac
        FROM AssignmentCheck ac
        JOIN FETCH ac.assignment a
        LEFT JOIN FETCH ac.assignmentContents acc
        WHERE ac.user.userId = :userId AND a.classRoom.classRoomId = :classId
    """)
  List<AssignmentCheck> findWithContentsByUserAndClass(@Param("userId") Long userId, @Param("classId") Long classId);
}
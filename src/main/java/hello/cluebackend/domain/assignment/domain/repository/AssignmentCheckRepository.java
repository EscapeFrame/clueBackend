package hello.cluebackend.domain.assignment.domain.repository;

import hello.cluebackend.domain.assignment.domain.Assignment;
import hello.cluebackend.domain.assignment.domain.AssignmentCheck;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentCheckRepository extends JpaRepository<AssignmentCheck,Long> {
  @Query("""
SELECT ac.assignment
FROM AssignmentCheck ac
JOIN ac.assignment a
JOIN a.classRoom cr
JOIN cr.classRoomUserList cru
WHERE ac.user.userId = :userId
  AND ac.isSubmitted = false
""")
  List<Assignment> findUnsubmittedAssignmentsByUserId(@Param("userId") Long userId);
}

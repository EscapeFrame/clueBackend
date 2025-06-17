package hello.cluebackend.domain.classroomuser.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRoomUserRepository extends JpaRepository<ClassRoomUser, Long> {
}

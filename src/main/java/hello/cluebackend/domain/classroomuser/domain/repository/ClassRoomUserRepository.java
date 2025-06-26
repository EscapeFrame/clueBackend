package hello.cluebackend.domain.classroomuser.domain.repository;

import hello.cluebackend.domain.classroomuser.domain.ClassRoomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRoomUserRepository extends JpaRepository<ClassRoomUser, Long> {

    List<ClassRoomUser> findByUser_UserId(Long userId);

}
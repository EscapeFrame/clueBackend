package hello.cluebackend.domain.classroomuser.domain.repository;

import hello.cluebackend.domain.classroom.domain.ClassRoom;
import hello.cluebackend.domain.classroomuser.domain.ClassRoomUser;
import hello.cluebackend.domain.user.domain.UserEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRoomUserRepository extends JpaRepository<ClassRoomUser, Long> {

    List<ClassRoomUser> findByUser_UserId(Long userId);

    @Query("SELECT cu.classRoom FROM ClassRoomUser cu WHERE cu.user.userId = :userId")
    List<ClassRoom> findClassRoomsByUserId(@Param("userId") Long userId);

    @Query("select cu.user from ClassRoomUser cu where cu.classRoom.classRoomId = :classId")
    List<UserEntity> findUsersByClassRoomId(@Param("classId") Long classId);

    @Query("SELECT cru.user FROM ClassRoomUser cru WHERE cru.classRoom.classRoomId = :classRoomId AND cru.user.role = hello.cluebackend.domain.user.domain.Role.STUDENT")
    List<UserEntity> findAllStudentsByClassRoomId(Long classRoomId);

    List<ClassRoomUser> user(UserEntity user);
}
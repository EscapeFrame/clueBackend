package hello.cluebackend.infrastructure.persistence.classroom;

import hello.cluebackend.domain.classroom.model.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClassRoomJpaRepository extends JpaRepository<ClassRoom, UUID> {
    Boolean existsByCode(String code);

    Optional<ClassRoom> findByCode(String code);

    ClassRoom findByClassRoomId(UUID classRoomId);

    @Query("select c from ClassRoom c" +
            " join fetch c.classRoomUserList cu" +
            " join fetch cu.user u" +
            " where c.classRoomId =:classRoomId" +
            " and u.role = hello.cluebackend.domain.user.model.Role.TEACHER")
    ClassRoom findByIdWithTeachers(@Param("classRoomId") UUID classRoomId);
}

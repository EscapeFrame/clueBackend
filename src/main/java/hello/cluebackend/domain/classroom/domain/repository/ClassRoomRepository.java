package hello.cluebackend.domain.classroom.domain.repository;

import hello.cluebackend.domain.classroom.domain.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, UUID> {
    Boolean existsByCode(String code);

    Optional<ClassRoom> findByCode(String code);

  ClassRoom findByClassRoomId(UUID classRoomId);
}

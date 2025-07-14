package hello.cluebackend.domain.classroom.domain.repository;

import hello.cluebackend.domain.classroom.domain.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {
    Boolean existsByCode(String code);

    Optional<ClassRoom> findByCode(String code);
}

package hello.cluebackend.infrastructure.persistence.quizroom;

import hello.cluebackend.domain.quizbattle.model.QuizRoom;
import hello.cluebackend.domain.quizbattle.model.QuizRoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuizRoomJpaRepository extends JpaRepository<QuizRoom, UUID> {

    Boolean existsByRoomCode(String roomCode);

    Optional<QuizRoom> findByRoomCode(String roomCode);

    @Query("SELECT q FROM QuizRoom q WHERE q.status IN :statuses ORDER BY q.createdAt DESC")
    List<QuizRoom> findByStatusIn(@Param("statuses") List<QuizRoomStatus> statuses);

    @Query("SELECT q FROM QuizRoom q WHERE q.host.userId = :hostId ORDER BY q.createdAt DESC")
    List<QuizRoom> findByHostId(@Param("hostId") UUID hostId);

    @Query("SELECT q FROM QuizRoom q WHERE q.classRoom.classRoomId = :classRoomId AND q.status IN :statuses")
    List<QuizRoom> findByClassRoomIdAndStatusIn(
            @Param("classRoomId") UUID classRoomId,
            @Param("statuses") List<QuizRoomStatus> statuses
    );

    @Query("SELECT q FROM QuizRoom q JOIN FETCH q.host WHERE q.roomCode = :roomCode")
    Optional<QuizRoom> findByRoomCodeWithHost(@Param("roomCode") String roomCode);
}

package hello.cluebackend.domain.notice.persistence;

import hello.cluebackend.domain.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, UUID> {
    @Query("select n from Notice n" +
            " join fetch n.user u" +
            " where u.userId = :userId")
    List<Notice> findAllByUserId(UUID userId);
}

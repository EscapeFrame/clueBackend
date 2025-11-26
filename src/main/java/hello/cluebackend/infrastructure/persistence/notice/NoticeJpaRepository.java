package hello.cluebackend.infrastructure.persistence.notice;

import hello.cluebackend.domain.notice.model.Notice;
import hello.cluebackend.domain.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NoticeJpaRepository extends JpaRepository<Notice, UUID> {
    @Query("select n from Notice n" +
            " join fetch n.user u" +
            " where u.userId = :userId")
    List<Notice> findAllByUserId(UUID userId);

    @Query("select count(n) from Notice n" +
            " join n.user u" +
            " where n.noticeId = :noticeId and u.userId = :userId")
    Long findMyNoticeByUserId(UUID userId, UUID noticeId);

//    Boolean existsByNoticeDocument_noticeDocumentId(UUID noticeDocumentId);

    void deleteByUser(UserEntity user);
}

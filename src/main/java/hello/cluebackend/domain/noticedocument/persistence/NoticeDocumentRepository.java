package hello.cluebackend.domain.noticedocument.persistence;

import hello.cluebackend.domain.noticedocument.domain.NoticeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NoticeDocumentRepository extends JpaRepository<NoticeDocument, UUID> {
    List<NoticeDocument> findByNotice_NoticeId(UUID noticeId);
}
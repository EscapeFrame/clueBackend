package hello.cluebackend.domain.noticedocument.persistence;

import hello.cluebackend.domain.noticedocument.domain.NoticeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeDocumentRepository extends JpaRepository<NoticeDocument, Long> {
}
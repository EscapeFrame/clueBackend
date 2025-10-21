package hello.cluebackend.domain.noticefile.persistence;

import hello.cluebackend.domain.noticefile.domain.NoticeFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeFileRepository extends JpaRepository<NoticeFile, Long> {
}
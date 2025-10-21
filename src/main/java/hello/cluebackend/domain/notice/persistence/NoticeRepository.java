package hello.cluebackend.domain.notice.persistence;

import hello.cluebackend.domain.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, UUID> {
}

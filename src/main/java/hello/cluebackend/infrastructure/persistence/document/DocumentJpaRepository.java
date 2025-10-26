package hello.cluebackend.infrastructure.persistence.document;

import hello.cluebackend.domain.document.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DocumentJpaRepository extends JpaRepository<Document, UUID> {
}

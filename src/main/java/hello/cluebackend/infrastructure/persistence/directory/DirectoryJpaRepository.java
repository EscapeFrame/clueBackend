package hello.cluebackend.infrastructure.persistence.directory;

import hello.cluebackend.domain.directory.model.Directory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DirectoryJpaRepository extends JpaRepository<Directory, UUID> {
}

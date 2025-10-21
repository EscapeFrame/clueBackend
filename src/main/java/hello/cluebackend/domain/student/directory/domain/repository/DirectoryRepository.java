package hello.cluebackend.domain.student.directory.domain.repository;

import hello.cluebackend.domain.student.directory.domain.Directory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DirectoryRepository extends JpaRepository<Directory, UUID> {
}

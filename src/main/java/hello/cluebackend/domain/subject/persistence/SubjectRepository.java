package hello.cluebackend.domain.subject.persistence;

import hello.cluebackend.domain.subject.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
  List<Subject> findAllByGrade(int grade);
}

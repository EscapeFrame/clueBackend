package hello.cluebackend.infrastructure.persistence.subject;

import hello.cluebackend.domain.subject.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectJpaRepository extends JpaRepository<Subject, Long> {
  List<Subject> findAllByGrade(int grade);
}

package hello.cluebackend.domain.subject.domain.repository;

import hello.cluebackend.domain.subject.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
  List<Subject> findAllByGrade(int grade);
}

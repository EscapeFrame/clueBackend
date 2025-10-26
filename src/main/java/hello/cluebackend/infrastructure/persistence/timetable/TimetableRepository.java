package hello.cluebackend.infrastructure.persistence.timetable;

import hello.cluebackend.domain.subject.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimetableRepository extends JpaRepository<Subject, Long> {
}
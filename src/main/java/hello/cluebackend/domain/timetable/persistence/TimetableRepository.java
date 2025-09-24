package hello.cluebackend.domain.timetable.persistence;

import hello.cluebackend.domain.subject.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimetableRepository extends JpaRepository<Subject, Long> {
}
package hello.cluebackend.domain.student.timetable.domain.repository;

import hello.cluebackend.domain.student.subject.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimetableRepository extends JpaRepository<Subject, Long> {
}
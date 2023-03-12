package study.recordservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.recordservice.domain.Visit;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    Optional<Visit> findByDate(LocalDateTime date);
}

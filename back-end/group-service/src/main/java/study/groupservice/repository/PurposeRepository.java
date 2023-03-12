package study.groupservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import study.groupservice.domain.Purpose;

import java.util.List;
import java.util.Optional;

public interface PurposeRepository extends JpaRepository<Purpose, Long> {
    Optional<Purpose> findByContent(String content);

    Page<Purpose> findByContentContaining(String content, Pageable pageable);
    List<Purpose> findByContentContaining(String content);

}

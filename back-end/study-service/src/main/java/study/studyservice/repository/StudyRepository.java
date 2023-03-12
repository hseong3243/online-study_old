package study.studyservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.studyservice.domain.Study;

import java.util.List;
import java.util.Optional;

public interface StudyRepository extends JpaRepository<Study, Long> {

    @Query("select s from Study s where s.name like %:studyName%")
    Page<Study> findAllByName(@Param("studyName") String studyName, Pageable pageable);

    Optional<Study> findByName(String name);

    @Query("select s from Study s where s.id in :studyIds")
    Page<Study> findAllById(@Param("studyIds") List<Long> studyIds, Pageable pageable);
}

package study.recordservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.recordservice.domain.Record;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long>, RecordRepositoryCustom {

    List<Record> findAllByMemberIdAndDateBetween(Long memberId, LocalDate start, LocalDate end);

    List<Record> findAllByStudyIdAndDateBetween(Long studyId, LocalDate start, LocalDate end);

    @Query("select r from Record r where r.date between :start and :end")
    List<Record> findByDate(LocalDate start, LocalDate end);

    @Query("select r from Record r where r.date = :date and r.memberId=:memberId and r.startTime >= :start and r.endTime < :end order by r.startTime asc")
    List<Record> findByDateAndTime(@Param(value = "date") LocalDate date, @Param(value = "memberId") String memberId,
                                   @Param(value = "start") long start, @Param(value = "end") long end);

    @Modifying
    @Query("delete from Record r where r.memberId in :memberIds")
    void deleteTestData(@Param(value = "memberIds") List<String> memberIds);
}

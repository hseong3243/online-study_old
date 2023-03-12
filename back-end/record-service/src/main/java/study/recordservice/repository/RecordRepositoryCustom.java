package study.recordservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.recordservice.domain.StudyCategory;
import study.recordservice.domain.StudyStatus;
import study.recordservice.dto.RecordAvgDto;
import study.recordservice.dto.RecordCount;
import study.recordservice.dto.RecordDto;
import study.recordservice.dto.RecordRankedDto;

import java.time.LocalDate;
import java.util.List;

public interface RecordRepositoryCustom {

    List<RecordDto> findAllRecord(String memberId, Long studyId, LocalDate start, LocalDate end);


    List<RecordDto> findAllRecord(Long studyId, LocalDate start, LocalDate end);

    List<RecordDto> findAllMembersRecord(List<String> memberIds, Long studyId, LocalDate start, LocalDate end);

    List<RecordAvgDto> findAvgRecordByStudyCategory(StudyCategory studyCategory, LocalDate start, LocalDate end);

    Page<RecordRankedDto> findTimeRecord(LocalDate start, LocalDate end, StudyStatus studyStatus,
                                         StudyCategory studyCategory, String memberId, Long studyId, Pageable pageable);

    Page<RecordRankedDto> findTimeRecordByDate(LocalDate start, LocalDate end, StudyStatus studyStatus,
                                               StudyCategory studyCategory, String memberId, Long studyId, Pageable pageable);

    RecordCount findCount(LocalDate start, LocalDate end, Long studyId);
}

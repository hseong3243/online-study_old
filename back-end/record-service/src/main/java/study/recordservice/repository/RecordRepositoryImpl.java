package study.recordservice.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import study.recordservice.domain.StudyCategory;
import study.recordservice.domain.StudyStatus;
import study.recordservice.dto.RecordAvgDto;
import study.recordservice.dto.RecordCount;
import study.recordservice.dto.RecordDto;
import study.recordservice.dto.RecordRankedDto;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static study.recordservice.domain.QRecord.*;


public class RecordRepositoryImpl implements RecordRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public RecordRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }


    @Override
    public List<RecordDto> findAllRecord(String memberId, Long studyId, LocalDate start, LocalDate end) {
        List<RecordDto> result = query
                .select(Projections.bean(RecordDto.class, record.studyId,
                        record.studyStatus, record.date, record.startTime, record.endTime))
                .from(record)
                .where(memberIdEq(memberId),
                        studyIdEq(studyId),
                        record.date.between(start, end))
                .orderBy(record.date.asc())
                .fetch();

        return result;
    }

    private BooleanExpression recordEndTimeLt(LocalDate start, LocalDate end) {
       return start==end ? record.endTime.lt(3600*24) : record.endTime.lt(3600*29-60);
    }

    @Override
    public List<RecordDto> findAllRecord(Long studyId, LocalDate start, LocalDate end) {
        List<RecordDto> result = query
                .select(Projections.bean(RecordDto.class, record.studyId, record.memberId,
                        record.studyStatus, record.date, record.startTime, record.endTime))
                .from(record)
                .where(studyIdEq(studyId),
                        record.date.between(start, end))
                .orderBy(record.date.asc())
                .fetch();

        return result;
    }


    @Override
    public List<RecordDto> findAllMembersRecord(List<String> memberIds, Long studyId, LocalDate start, LocalDate end) {
        List<RecordDto> result = query
                .select(Projections.bean(RecordDto.class, record.studyId, record.memberId,
                        record.studyStatus, record.date, record.startTime, record.endTime))
                .from(record)
                .where(record.memberId.in(memberIds),
                        studyIdEq(studyId),
                        record.date.between(start, end))
                .orderBy(record.date.asc())
                .fetch();
        return result;
    }

    @Override
    public List<RecordAvgDto> findAvgRecordByStudyCategory(StudyCategory studyCategory, LocalDate start, LocalDate end) {
        List<RecordAvgDto> result = query
                .select(Projections.bean(RecordAvgDto.class,
                        record.date, record.startTime.min().as("startTime"), record.endTime.max().as("endTime")))
                .from(record)
                .where(record.date.between(start, end),
                        record.studyCategory.eq(studyCategory))
                .orderBy(record.date.asc())
                .groupBy(record.date,
                        record.memberId)
                .fetch();
        return result;
    }

    @Override
    public Page<RecordRankedDto> findTimeRecord(LocalDate start, LocalDate end, StudyStatus studyStatus,
                                                StudyCategory studyCategory, String memberId, Long studyId, Pageable pageable) {
        List<RecordRankedDto> result = query
                .select(Projections.bean(RecordRankedDto.class, record.memberId, record.studyCategory,
                        record.studyStatus, record.startTime.sum().as("startTime"), record.endTime.sum().as("endTime"),
                        record.endTime.sum().subtract(record.startTime.sum()).as("studyTime")))
                .from(record)
                .where(record.date.between(start, end), record.studyStatus.eq(studyStatus),
                        memberIdEq(memberId), studyCategoryEq(studyCategory), studyIdEq(studyId))
                .groupBy(record.memberId)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = query
                .select(record.memberId.countDistinct())
                .from(record)
                .where(record.date.between(start, end), record.studyStatus.eq(studyStatus),
                        memberIdEq(memberId), studyCategoryEq(studyCategory), studyIdEq(studyId))
                .groupBy(record.memberId)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchOne();

        if(result.size() == 0) {
            result = List.of(RecordRankedDto.create(memberId, studyCategory, studyStatus, 0, 0));
            count = 0L;
        }

        return new PageImpl<>(result, pageable, count);
    }

    @Override
    public Page<RecordRankedDto> findTimeRecordByDate(LocalDate start, LocalDate end, StudyStatus studyStatus,
                                                      StudyCategory studyCategory, String memberId, Long studyId, Pageable pageable) {
        Long count = query
                .select(record.countDistinct())
                .from(record)
                .where(record.date.between(start, end), record.studyStatus.eq(studyStatus),
                        memberIdEq(memberId), studyCategoryEq(studyCategory), studyIdEq(studyId))
                .fetchOne();

        if(count == null)
            count = 0L;
        long limit = count>0 ? count : 1;

        List<RecordRankedDto> result = query
                .select(Projections.bean(RecordRankedDto.class, record.memberId, record.studyCategory,
                        record.studyStatus, record.startTime.sum().as("startTime"), record.endTime.sum().as("endTime"),
                        record.endTime.sum().subtract(record.startTime.sum()).as("studyTime"), record.date))
                .from(record)
                .where(record.date.between(start, end), record.studyStatus.eq(studyStatus),
                        memberIdEq(memberId), studyCategoryEq(studyCategory), studyIdEq(studyId))
                .groupBy(record.date, record.memberId)
                .orderBy(record.endTime.sum().subtract(record.startTime.sum()).desc())
                .limit((long) Math.ceil(limit*0.1))
                .fetch();


        return new PageImpl<>(result, pageable, count);
    }

    @Override
    public RecordCount findCount(LocalDate start, LocalDate end, Long studyId) {
        RecordCount recordCount = query
                .select(Projections.bean(RecordCount.class, record.memberId.countDistinct().as("studyCount")))
                .from(record)
                .where(record.date.between(start, end), studyIdEq(studyId))
                .fetchOne();

        return recordCount;
    }

    private BooleanExpression studyCategoryEq(StudyCategory studyCategory) {
        return studyCategory == null ? null : record.studyCategory.eq(studyCategory);
    }

    private BooleanExpression memberIdEq(String memberId) {
        return memberId == null ? null : record.memberId.eq(memberId);
    }

    private BooleanExpression studyIdEq(Long studyId) {
        return studyId == null ? null : record.studyId.eq(studyId);
    }
}

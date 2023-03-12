package study.recordservice.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.recordservice.domain.QRecord;
import study.recordservice.domain.StudyCategory;
import study.recordservice.domain.StudyStatus;
import study.recordservice.domain.Record;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static study.recordservice.domain.QRecord.*;

@SpringBootTest
@Transactional
class RecordServiceTest {

    @PersistenceContext
    EntityManager em;
    private JPAQueryFactory query;

    @BeforeEach
    public void init() {
        query = new JPAQueryFactory(em);

    }

    @Test
    public void findRecord() {
        LocalDate localDate = LocalDate.now();
        long startTime = LocalDateTime.now().minusMinutes(30).toEpochSecond(ZoneOffset.of("+09:00"));
        long endTime = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+09:00"));
        String uuid = UUID.randomUUID().toString();

        Record record1 = new Record(StudyStatus.STUDY,
                StudyCategory.UNIV,
                startTime,
                endTime,
                LocalDate.now(), uuid, 1L);

        Record record2 = new Record(StudyStatus.STUDY,
                StudyCategory.UNIV,
                startTime,
                endTime,
                LocalDate.now(), uuid, 1L);

        Record record3 = new Record(StudyStatus.STUDY,
                StudyCategory.UNIV,
                startTime,
                endTime,
                LocalDate.now(), uuid, 1L);
        Record record4 = new Record(StudyStatus.STUDY,
                StudyCategory.UNIV,
                LocalDateTime.now().minusMinutes(30).toEpochSecond(ZoneOffset.of("+09:00")),
                endTime,
                LocalDate.now(), uuid, 2L);
        Record record5 = new Record(StudyStatus.STUDY,
                StudyCategory.UNIV,
                LocalDateTime.now().minusMinutes(30).toEpochSecond(ZoneOffset.of("+09:00")),
                endTime,
                LocalDate.now(), uuid, 2L);

        em.persist(record1);
        em.persist(record2);
        em.persist(record3);
        em.persist(record4);
        em.persist(record5);

        List<Record> result = query.select(record)
                .from(record)
                .fetch();


        assertThat(result).contains(record1);

    }



}
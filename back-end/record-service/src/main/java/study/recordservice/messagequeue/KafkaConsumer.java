package study.recordservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.recordservice.client.MemberServiceClient;
import study.recordservice.domain.StudyCategory;
import study.recordservice.domain.StudyStatus;
import study.recordservice.domain.Record;
import study.recordservice.repository.RecordRepository;
import study.recordservice.repository.VisitRepository;

import java.time.*;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KafkaConsumer {

    private final RecordRepository recordRepository;
    private final VisitRepository visitRepository;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final MemberServiceClient memberServiceClient;

    @KafkaListener(topics = "study-ticket-expire")
    public void updateRecord(String message) {
        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(message, new TypeReference<Map<Object, Object>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Long ticketId = Long.valueOf((Integer) map.get("ticketId"));
        long startTimeEpoch = Long.parseLong((String) map.get("startTime"));
        long endTimeEpoch = Long.parseLong((String) map.get("endTime"));
        StudyStatus studyStatus = StudyStatus.valueOf((String) map.get("studyStatus"));
        StudyCategory studyCategory = StudyCategory.valueOf((String) map.get("studyCategory"));
        String memberId = (String) map.get("memberId");
        Long studyId = Long.valueOf((String) map.get("studyId"));

        LocalDate date = LocalDate.ofInstant(Instant.ofEpochSecond(startTimeEpoch), ZoneOffset.of("+09:00"));

        long startTime = startTimeEpoch - date.atStartOfDay().toEpochSecond(ZoneOffset.of("+09:00"));
        long endTime = endTimeEpoch - date.atStartOfDay().toEpochSecond(ZoneOffset.of("+09:00"));



        if(startTime < 3600*5) {
            startTime = startTime+3600*24;
            date = date.minusDays(1);
            if(endTime < 3600*5) {
                endTime = endTime+3600*24;
            }
            else {
                endTime = 3600*5+3600*24-60;
            }
        }

        if(endTime-startTime < 3600*24) {
            Record record
                    = new Record(studyStatus, studyCategory, startTime, endTime, date, memberId, studyId);
            recordRepository.save(record);


            log.info("만료된 티켓이 기록되었습니다. ticketId={}", ticketId);
        }
        else {
            log.info("티켓의 상태가 정상 종료되지 않았습니다. 티켓을 기록하지 않습니다. ticketId={}", ticketId);
        }



    }

    @KafkaListener(topics = "member-time-update")
    public void updateTargetTime(String message) {
        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(message, new TypeReference<Map<Object, Object>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String memberId = (String) map.get("memberId");

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
    }

    @KafkaListener(topics = "member-member-login")
    public void updateVisit(String message) {
        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(message, new TypeReference<Map<Object, Object>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String memberId = (String) map.get("memberId");


    }

}

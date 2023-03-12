package study.groupservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.groupservice.domain.GroupMember;
import study.groupservice.repository.GroupMemberRepository;
import study.groupservice.repository.GroupRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KafkaConsumer {

    private final GroupMemberRepository groupMemberRepository;
    private final GroupRepository groupRepository;

    @KafkaListener(topics = "study-ticket-issue")
    public void updateGroupMember(String message) {
        Map<Object, Object> map = messageToMapper(message);

        log.info("새로운 티켓 발행");

        long startTimeEpoch = Long.parseLong((String) map.get("startTime"));
        Long studyId = Long.valueOf((String) map.get("studyId"));
        String memberId = (String) map.get("memberId");

        groupMemberRepository.findGroupMemberAttend(memberId, studyId).ifPresent(attend -> {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime start = LocalDate.now().atStartOfDay().plusHours(5);
            if(now.getHour()<5){
                start = start.minusDays(1);
            }

            if(attend.isBefore(start)) {
                GroupMember groupMember = groupMemberRepository.findByMemberIdAndAttend(memberId, attend).get();
                groupMember.setAttend(LocalDateTime.ofEpochSecond(startTimeEpoch, 0, ZoneOffset.of("+09:00")));
                log.info("멤버의 출석이 기록되었습니다. groupMember={}", groupMember);
            }
        });
    }

    @KafkaListener(topics = "group-group-commit")
    public void commitGroup(String message) {
        Map<Object, Object> map = messageToMapper(message);

        Long groupId = Long.valueOf((Integer) map.get("groupId"));

        log.info("=====[Commit] group-create-commit, groupId={}=====", groupId);
    }

    @KafkaListener(topics = "group-group-rollback")
    public void rollBackGroup(String message) {
        Map<Object, Object> map = messageToMapper(message);

        Long groupId = Long.valueOf((Integer) map.get("groupId"));
        groupRepository.deleteById(groupId);

        log.error("=====[Rollback] group-create-rollback, groupId={}=====", groupId);
    }

    private Map<Object, Object> messageToMapper(String message) {
        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(message, new TypeReference<Map<Object, Object>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return map;
    }
}

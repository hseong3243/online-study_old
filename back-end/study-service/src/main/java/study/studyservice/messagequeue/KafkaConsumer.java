package study.studyservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.studyservice.domain.Room;
import study.studyservice.repository.RoomRepository;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KafkaConsumer {

    private final RoomRepository roomRepository;
    private final KafkaProducer producer;

    @KafkaListener(topics = "group-group-create")
    public void updateTicketRecord(String message) {
        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(message, new TypeReference<Map<Object, Object>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            Long groupId = Long.valueOf((Integer) map.get("groupId"));
            String name = (String) map.get("name");
            int level = (Integer) map.get("level");

            for (int i = 1; i <= level * 2; i++) {
                String newName = name + " " + i + "번방";
                Room groupRoom = Room.createGroupRoom(newName, groupId);
                roomRepository.save(groupRoom);
                log.info("그룹 스터디룸이 생성되었습니다. groupId={}", groupRoom.getId());
            }
            producer.sendRawMessage("group-group-commit", message); // Commit
        }
        catch (Exception e) {
            producer.sendRawMessage("group-group-rollback", message); // Rollback
            log.error(e.getMessage());
        }
    }
}

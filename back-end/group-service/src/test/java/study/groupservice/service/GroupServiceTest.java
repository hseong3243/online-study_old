package study.groupservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import study.groupservice.client.MemberServiceClient;
import study.groupservice.client.RecordServiceClient;
import study.groupservice.client.StudyServiceClient;
import study.groupservice.messagequeue.KafkaProducer;
import study.groupservice.repository.GroupMemberRepository;
import study.groupservice.repository.GroupPurposeRepository;
import study.groupservice.repository.GroupRepository;
import study.groupservice.repository.PurposeRepository;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @InjectMocks
    GroupService groupService;

    @Mock
    GroupRepository groupRepository;
    @Mock
    GroupMemberRepository groupMemberRepository;
    @Mock
    MemberServiceClient memberServiceClient;
    @Mock
    RecordServiceClient recordServiceClient;
    @Mock
    StudyServiceClient studyServiceClient;
    @Mock
    PurposeRepository purposeRepository;
    @Mock
    GroupPurposeRepository groupPurposeRepository;
    @Mock
    KafkaProducer kafkaProducer;
    @Mock
    CircuitBreakerFactory circuitBreakerFactory;


    @Test
    void getGroups() {

    }
}
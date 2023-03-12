package study.memberservice.messagequeue;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import study.memberservice.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final MemberRepository memberRepository;


}

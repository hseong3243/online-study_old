package study.memberservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.memberservice.domain.Member;
import study.memberservice.domain.MemberInfo;
import study.memberservice.messagequeue.KafkaProducer;
import study.memberservice.messagequeue.MemberMessage;
import study.memberservice.params.InfoParam;
import study.memberservice.repository.MemberInfoRepository;
import study.memberservice.repository.MemberRepository;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class MemberInfoService {

    private final MemberInfoRepository memberInfoRepository;
    private final MemberRepository memberRepository;
    private final KafkaProducer producer;

    @Transactional
    public Long createInfo(InfoParam infoParam) {
        Member member = memberRepository.findByMemberId(infoParam.getMemberId()).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 멤버입니다.");
        });
        MemberInfo memberInfo = MemberInfo.create(infoParam);

        memberInfoRepository.save(memberInfo);
        member.setMemberInfo(memberInfo);

        return memberInfo.getId();
    }

    @Transactional
    public Long updateInfo(String memberId, InfoParam infoParam) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 멤버입니다.");
        });

        MemberInfo memberInfo = member.getMemberInfo();

        if(infoParam.getPromise() != null) {
            memberInfo.changePromise(infoParam.getPromise());
        }
        if(infoParam.getDDay() != null) {
            memberInfo.changeDDay(infoParam.getDDay());
        }
        if(infoParam.getGoal() != null) {
            memberInfo.changeGoal(infoParam.getGoal());
        }
        if(infoParam.getTargetTime() != null) {
            memberInfo.changeTargetTime(infoParam.getTargetTime());
            MemberMessage message = new MemberMessage();
            message.setMemberId(memberId);
            producer.sendId("member-time-update", message);
        }

        return memberInfo.getId();
    }
}

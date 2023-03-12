package study.memberservice;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import study.memberservice.domain.Member;
import study.memberservice.domain.MemberInfo;
import study.memberservice.domain.StudyCategory;
import study.memberservice.dto.MemberDto;
import study.memberservice.repository.MemberRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InitData {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

//    @PostConstruct
    public void init() {
        List<Member> members = new ArrayList<>();

        for(int i=0; i<100; i++) {
            String name = UUID.randomUUID().toString().substring(0, 8);
            String pwd = UUID.randomUUID().toString().substring(0, 8);

            MemberDto memberDto = new MemberDto();
            memberDto.setEmail(name+"@naver.com");
            memberDto.setName(name);
            memberDto.setPassword(encoder.encode(pwd));
            memberDto.setStudyCategory(StudyCategory.values()[new Random().nextInt(StudyCategory.values().length)]);
            Member member = Member.create(memberDto);

            MemberInfo memberInfo = new MemberInfo();
            member.setMemberInfo(memberInfo);

            members.add(member);
        }

        memberRepository.saveAll(members);
    }
}

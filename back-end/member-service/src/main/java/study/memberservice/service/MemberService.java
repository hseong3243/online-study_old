package study.memberservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.memberservice.dto.GroupDto;
import study.memberservice.client.GroupServiceClient;
import study.memberservice.dto.Result;
import study.memberservice.domain.Member;
import study.memberservice.domain.MemberInfo;
import study.memberservice.dto.MemberDto;
import study.memberservice.params.MemberParams;
import study.memberservice.repository.MemberInfoRepository;
import study.memberservice.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final GroupServiceClient groupServiceClient;
    private final MemberInfoRepository memberInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username).orElseThrow(() -> {
            throw new UsernameNotFoundException(username);
        });

        return new User(member.getEmail(), member.getPassword(), new ArrayList<>());
    }

    @Transactional
    public MemberDto createMember(MemberDto memberDto) {
        String encryptedPassword = passwordEncoder.encode(memberDto.getPassword());
        memberDto.setPassword(encryptedPassword);
        Member member = Member.create(memberDto);

        MemberInfo memberInfo = new MemberInfo();
        memberInfoRepository.save(memberInfo);
        memberRepository.save(member);

        member.setMemberInfo(memberInfo);

        return MemberDto.from(member);
    }

    public MemberDto getMemberByEmail(String username) {
        Member member = memberRepository.findByEmail(username).orElseThrow(() -> {
            throw new UsernameNotFoundException(username);
        });

        return MemberDto.from(member);
    }

    public List<MemberDto> getMembers() {
        List<Member> members = memberRepository.findAll();

        List<MemberDto> result = new ArrayList<>();
        members.forEach(m -> {
            result.add(MemberDto.from(m));
        });

        return result;
    }

    public MemberDto getMemberById(String memberId) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 멤버입니다.");
        });
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        log.info("Before call group-service");
        Result<List<GroupDto>> result = circuitBreaker.run(() -> groupServiceClient.getGroups(memberId, 0, 20),
                throwable -> new Result<>());
        log.info("After call group-service");
        List<GroupDto> groupDtos = result.getData();

        MemberDto memberDto = MemberDto.from(member);
        memberDto.setGroups(groupDtos);

        return memberDto;
    }

    public List<MemberDto> getMembers(List<String> memberIds) {
        List<Member> members = memberRepository.findAllByMemberId(memberIds);
        List<MemberDto> memberDtos = new ArrayList<>();
        members.forEach(member -> {
            memberDtos.add(MemberDto.from(member));
        });
        return memberDtos;
    }

    @Transactional
    public Long updateMember(String memberId, MemberParams memberParams) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 유저입니다.");
        });

        if(memberParams.getName() != null) {
            member.changeName(memberParams.getName());
        }
        else if(memberParams.getStudyCategory() != null) {
            member.changeCategory(memberParams.getStudyCategory());
        }
        else if(memberParams.getPassword() != null) {
            if(!memberParams.getUpdatePassword().equals(memberParams.getUpdatePasswordCheck())) {
                throw new RuntimeException("변경하려는 비밀번호와 일치하지 않습니다.");
            }

            member.changePassword(passwordEncoder.encode(memberParams.getUpdatePassword()));
        }

        return member.getId();
    }

    public Long checkPassword(String memberId, MemberParams memberParams) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 유저입니다.");
        });
        boolean matches = passwordEncoder.matches(memberParams.getPassword(), member.getPassword());
        if(matches) {
            return member.getId();
        }
        else {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }
    }

    @Transactional
    public void updateLastLoginAt(String memberId) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 유저입니다.");
        });

        member.updateLastLogin();
    }
}

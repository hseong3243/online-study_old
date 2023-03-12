package study.memberservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.memberservice.dto.MemberDto;
import study.memberservice.params.MemberParams;
import study.memberservice.service.MemberService;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final Environment env;

    @GetMapping("/")
    public String hello() {
        return "hello";
    }

    @GetMapping("/check")
    public String check() {
        String tokenKey = env.getProperty("token.secret");
        return "this is token key " + tokenKey;
    }

    @PostMapping("/members")
    public ResponseEntity<MemberDto> createMember(@RequestBody MemberDto memberDto) {
        MemberDto responseMember = memberService.createMember(memberDto);

        return ResponseEntity.status(CREATED).body(responseMember);
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberDto>> getMembers(@RequestParam(required = false) List<String> memberIds) {
        List<MemberDto> responseMembers;
        if(memberIds != null) {
            responseMembers = memberService.getMembers(memberIds);
        } else {
            responseMembers = memberService.getMembers();
        }

        return ResponseEntity.status(OK).body(responseMembers);
    }

    @GetMapping("/members/{memberId}")
    public ResponseEntity<MemberDto> getMember(@PathVariable String memberId) {
        MemberDto memberDto = memberService.getMemberById(memberId);

        return ResponseEntity.status(OK).body(memberDto);
    }

    @PostMapping("/members/{memberId}/check")
    public ResponseEntity<Long> checkPassword(@PathVariable String memberId,
                                             @RequestBody MemberParams memberParams) {

        Long updatedMemberId = memberService.checkPassword(memberId, memberParams);

        return ResponseEntity.status(OK).body(updatedMemberId);
    }

    @PostMapping("/members/{memberId}")
    public ResponseEntity<Long> updateMember(@PathVariable String memberId,
                                             @RequestBody MemberParams memberParams) {
        Long updatedMemberId = memberService.updateMember(memberId, memberParams);

        return ResponseEntity.status(OK).body(updatedMemberId);
    }



}

package study.recordservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import study.recordservice.dto.MemberDto;

import java.util.List;

@FeignClient(name = "member-service")
public interface MemberServiceClient {

    @GetMapping("/members")
    List<MemberDto> getMembers(@RequestParam(name = "memberIds") List<String> memberIds);

    @GetMapping("/members/{memberId}")
    MemberDto getMember(@PathVariable(name = "memberId") String memberId);

    @GetMapping("/test/testMembers")
    List<MemberDto> testMembers();
}

package study.groupservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import study.groupservice.dto.MemberDto;

import java.util.List;

@FeignClient(name = "member-service")
public interface MemberServiceClient {

    @GetMapping("/members/{memberId}")
    MemberDto getMember(@PathVariable(value = "memberId") String memberId);

    @GetMapping("/members")
    List<MemberDto> getMembers(@RequestParam(value = "memberIds") List<String> memberIds);
}

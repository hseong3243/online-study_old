package study.memberservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import study.memberservice.dto.GroupDto;
import study.memberservice.dto.Result;

import java.util.List;

@FeignClient(name = "group-service")
public interface GroupServiceClient {

    @GetMapping("/groups")
    Result<List<GroupDto>> getGroups(@RequestParam(name="memberId") String memberId,
                                     @RequestParam(name="page") int page,
                                     @RequestParam(name = "size") int size);
}

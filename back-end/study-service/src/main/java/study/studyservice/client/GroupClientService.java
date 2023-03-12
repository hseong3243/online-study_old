package study.studyservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import study.studyservice.dto.GroupDto;

@FeignClient(name = "group-service")
public interface GroupClientService {
    @GetMapping("/groups/{groupId}")
    GroupDto getGroup(@PathVariable(name = "groupId") Long groupId);
}

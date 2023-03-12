package study.groupservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import study.groupservice.dto.Result;
import study.groupservice.dto.RoomDto;
import study.groupservice.dto.StudyDto;

import java.util.List;

@FeignClient(name = "study-service")
public interface StudyServiceClient {

    @GetMapping("/rooms")
    Result<List<RoomDto>> getRooms(@RequestParam(name = "groupId") Long groupId);

    @GetMapping("/studies")
    Result<List<StudyDto>> getStudies(@RequestParam(name = "studyIds") List<Long> studyIds);

    @GetMapping("/studies/{studyId}")
    StudyDto getStudy(@PathVariable(name="studyId") Long studyId);

    @GetMapping("/rooms")
    Result<List<RoomDto>> getRooms(@RequestParam(name = "groupIds") List<Long> groupIds, @RequestParam(name = "page") int page, @RequestParam(name = "size") int size);
}

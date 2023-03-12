package study.recordservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import study.recordservice.dto.Result;
import study.recordservice.dto.StudyDto;

import java.util.List;

@FeignClient(name = "study-service")
public interface StudyServiceClient {

    @GetMapping("/studies")
    Result<List<StudyDto>> getStudies(@RequestParam(name = "studyIds") List<Long> studyIds);

}

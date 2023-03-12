package study.groupservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import study.groupservice.dto.StudyRecord;

import java.time.LocalDate;

@FeignClient(name = "record-service")
public interface RecordServiceClient {

    @GetMapping("/records")
    StudyRecord getMemberRecords(@RequestParam(name = "memberId") String memberId,
                                 @RequestParam(name = "period") String period,
                                 @RequestParam(name = "date")
                                 @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate date,
                                 @RequestParam(name = "studyId") Long studyId);
}

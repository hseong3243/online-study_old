package study.recordservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.recordservice.dto.Result;
import study.recordservice.domain.StudyCategory;
import study.recordservice.domain.StudyStatus;
import study.recordservice.dto.*;
import study.recordservice.service.RecordService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class RecordController {

    private final RecordService recordService;

    @GetMapping("/records")
    public ResponseEntity<StudyRecord> getRecords(@RequestParam(name = "memberId", required = false) String memberId,
                                                  @RequestParam(name = "period") String period,
                                                  @RequestParam(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate date,
                                                  @RequestParam(name = "studyId", required = false) Long studyId,
                                                  @RequestParam(name = "groupId", required = false) Long groupId,
                                                  @RequestParam(name="studyCategory", required = false)StudyCategory studyCategory) {

        StudyRecord studyRecord;
        if(studyCategory != null) {
            studyRecord = recordService.getRecords(period, date, studyCategory);
        }
        else if(memberId != null && studyId == null){
            studyRecord = recordService.getRecords(memberId, period, date);
        }else if(memberId != null) {
            studyRecord = recordService.getRecords(memberId, period, date, studyId);
        }else if(studyId != null) {
            studyRecord = recordService.getRecords(period, date, studyId);
        }else if(groupId != null) {
            studyRecord = recordService.getRecords(groupId);
        }
        else
            throw new RuntimeException("잘못된 접근입니다.");

        return ResponseEntity.status(HttpStatus.OK).body(studyRecord);
    }

    @GetMapping("/records/ranked")
    public ResponseEntity<Result<List<RecordRankedResponse>>> getRankedRecord(@RequestParam(name = "period") String period,
                                                                         @RequestParam(name = "date")@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                                                         @RequestParam(name = "studyStatus")StudyStatus studyStatus,
                                                                         @RequestParam(name = "page") int page, @RequestParam(name = "size") int size,
                                                                         @RequestParam(name = "memberId", required = false) String memberId,
                                                                         @RequestParam(name = "studyId", required = false) Long studyId,
                                                                         @RequestParam(name = "groupId", required = false) Long groupId,
                                                                         @RequestParam(name = "studyCategory", required = false) StudyCategory studyCategory,
                                                                              @RequestParam(name = "byDate", required = false) String byDate){

        Result<List<RecordRankedResponse>> result;
        if(byDate == null)
            result = recordService.getRankedRecord(period, date, studyStatus, studyCategory, memberId, studyId, page, size);
        else
            result = recordService.getRankedRecordByDate(period, date, studyStatus, studyCategory, memberId, studyId, page, size);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @PostMapping("/records")
    public ResponseEntity<Long> createRecords(@RequestBody RecordParam recordParam) {
        Long recordId = recordService.createRecord(recordParam);

        return ResponseEntity.status(HttpStatus.CREATED).body(recordId);
    }

    @GetMapping("/records/{memberId}")
    public ResponseEntity<StudyRecord> getRawRecords(@PathVariable(name = "memberId") String memberId) {
        StudyRecord rawRecords = recordService.getRawRecords(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(rawRecords);
    }


    @GetMapping("/count")
    public ResponseEntity<RecordCount> getCount(@RequestParam(name = "period") String period,
                                                @RequestParam(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate date,
                                                @RequestParam(name = "studyId", required = false) Long studyId) {
        RecordCount count = recordService.getCount(period, date, studyId);

        return ResponseEntity.status(HttpStatus.OK).body(count);
    }

}

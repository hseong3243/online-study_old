package study.studyservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.studyservice.dto.*;
import study.studyservice.service.StudyService;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class StudyController {

    private final StudyService studyService;

    @GetMapping("/studies")
    public ResponseEntity<Result<List<StudyDto>>> getStudies(@RequestParam(name = "studyName", required = false) String studyName,
                                                             @RequestParam(name = "studyIds", required = false) List<Long> studyIds) {
        Page<StudyDto> studyDtos;
        Result<List<StudyDto>> result;
        if(studyName != null && studyIds != null)
            throw new RuntimeException("하나의 파라미터만 입력 가능합니다.");

        if(studyName != null) {
            studyDtos = studyService.getStudiesByStudyName(studyName);
            result = new Result<>(studyDtos.getContent(), studyDtos.getNumber(), studyDtos.getTotalPages());
        }
        else if(studyIds != null) {
            List<StudyDto> studies = studyService.getStudiesByStudyIds(studyIds);
            result = new Result<>(studies, 0, 0);
        }
        else {
            studyDtos = studyService.getStudies();
            result = new Result<>(studyDtos.getContent(), studyDtos.getNumber(), studyDtos.getTotalPages());
        }

        return ResponseEntity.status(OK).body(result);
    }

    @PostMapping("/studies")
    public ResponseEntity<Long> createStudy(@RequestBody StudyDto studyDto) {
        Long studyId = studyService.saveStudy(studyDto);

        return ResponseEntity.status(CREATED).body(studyId);
    }

    @GetMapping("/studies/{studyId}")
    public ResponseEntity<StudyDto> getStudy(@PathVariable(name = "studyId") Long studyId) {
        StudyDto studyDto = studyService.getStudy(studyId);

        return ResponseEntity.status(OK).body(studyDto);
    }
}

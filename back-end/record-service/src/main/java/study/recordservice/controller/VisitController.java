package study.recordservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.recordservice.dto.VisitDto;
import study.recordservice.service.VisitService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/visit")
public class VisitController {

    private final VisitService visitService;

    @GetMapping()
    public ResponseEntity<VisitDto> getVisited() {
        VisitDto visited = visitService.getVisited();

        return ResponseEntity.status(HttpStatus.OK).body(visited);
    }
}

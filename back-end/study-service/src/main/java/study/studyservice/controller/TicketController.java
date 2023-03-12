package study.studyservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.studyservice.dto.Result;
import study.studyservice.dto.StudyDto;
import study.studyservice.dto.TicketDto;
import study.studyservice.dto.TicketParam;
import study.studyservice.service.TicketService;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/tickets")
    public ResponseEntity<Long> issueTicket(@RequestBody TicketParam ticketParam) {
        Long ticketId = ticketService.issueTicket(ticketParam);

        return ResponseEntity.status(CREATED).body(ticketId);
    }

    @GetMapping("/tickets/studies")
    public ResponseEntity<Result<List<StudyDto>>> getTickets(@RequestParam(required = false, value = "memberId") String memberId,
                                                             @RequestParam(required = false, value = "day") Integer day){
        Result<List<StudyDto>> result;

        Page<StudyDto> studies;
        if(day == null)
            studies = ticketService.getStudiesByMemberId(memberId);
        else
            studies = ticketService.getStudiesByMemberId(memberId, day);

        result = new Result<>(studies.getContent(), studies.getNumber(), studies.getTotalPages());

        return ResponseEntity.status(OK).body(result);
    }

    @GetMapping("/tickets/{memberId}/valid")
    public ResponseEntity<TicketDto> getValidTicket(@PathVariable String memberId,
                                                    @RequestParam(name = "ticketStatus",required = false) String ticketStatus) {
        TicketDto ticketDto = ticketService.getValidTicket(memberId);

        return ResponseEntity.status(OK).body(ticketDto);
    }


    @GetMapping("/tickets/{ticketId}")
    public ResponseEntity<TicketDto> getTicket(@PathVariable Long ticketId) {
        TicketDto ticketDto = ticketService.getTicket(ticketId);

        return ResponseEntity.status(OK).body(ticketDto);
    }

    @PostMapping("/tickets/{ticketId}")
    public ResponseEntity<Long> updateTicket(@PathVariable Long ticketId,
                                             @RequestBody(required = false) Map<String, String> studyStatusMap) {

        Long ticket = ticketService.changeStudyStatus(ticketId, studyStatusMap.get("studyStatus"));

        return ResponseEntity.status(CREATED).body(ticket);
    }

    @PostMapping("/tickets/{ticketId}/expire")
    public ResponseEntity<Long> expireTicket(@PathVariable("ticketId") Long ticketId) {
        Long ticket = ticketService.expireTicket(ticketId);

        return ResponseEntity.status(OK).body(ticket);
    }
}

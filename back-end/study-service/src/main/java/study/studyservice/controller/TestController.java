package study.studyservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import study.studyservice.domain.Ticket;
import study.studyservice.dto.TicketDto;
import study.studyservice.service.TicketService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {

    private final SimpMessageSendingOperations simp;
    private final TicketService ticketService;

    @MessageMapping("/test")
    public void test(Message message) {
        log.info(message.toString());
        simp.convertAndSend("/topic/room/" + message.getRoomId(), message);
    }

    @MessageMapping("/update")
    public void update(Message message) {
        TicketDto ticketDto = ticketService.getTicket(message.getTicketId());
        simp.convertAndSend("/topic/room/"+message.getRoomId(), ticketDto);
    }
}

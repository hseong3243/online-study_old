package study.studyservice.controller;

import lombok.Data;
import study.studyservice.dto.TicketDto;

@Data
public class Message {

    private Long roomId;
    private Long ticketId;
}

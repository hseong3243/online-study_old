package study.studyservice.dto;

import lombok.Data;
import study.studyservice.domain.StudyStatus;
import study.studyservice.domain.Ticket;
import study.studyservice.domain.TicketStatus;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
public class TicketDto {

    private Long ticketId;
    private String memberName;
    private String roomName;
    private String studyName;
    private int seat;
    private long studyTime;
//    private long rootStartTime;
    private long startTime;
    private long endTime;
    private TicketStatus ticketStatus;
    private StudyStatus studyStatus;

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
    public void setStudyTime(long studyTime) {
        this.studyTime = studyTime;
    }

    public static TicketDto from(Ticket ticket) {
        TicketDto ticketDto = new TicketDto();
        ticketDto.ticketId = ticket.getId();
        ticketDto.roomName = ticket.getRoom().getName();
        ticketDto.studyName = ticket.getStudy().getName();
        ticketDto.seat = ticket.getSeat();
        ticketDto.studyTime = ticket.getStudyTime();
//        ticketDto.rootStartTime = ticket.getRootStartTime().toEpochSecond(ZoneOffset.of("+09:00"));
        ticketDto.startTime = ticket.getStartTime().toEpochSecond(ZoneOffset.of("+09:00"));
        ticketDto.endTime = ticket.getEndTime().toEpochSecond(ZoneOffset.of("+09:00"));
        ticketDto.ticketStatus = ticket.getTicketStatus();
        ticketDto.studyStatus = ticket.getStudyStatus();

        return ticketDto;
    }
}

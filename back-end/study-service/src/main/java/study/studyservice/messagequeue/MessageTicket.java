package study.studyservice.messagequeue;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import study.studyservice.domain.StudyCategory;
import study.studyservice.domain.StudyStatus;
import study.studyservice.domain.Ticket;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
public class MessageTicket {

    private Long ticketId;
    private StudyStatus studyStatus;
    private StudyCategory studyCategory;

    private String startTime;
    private String endTime;
    private String studyTime;

    private String memberId;
    private String studyId;

    public static MessageTicket from(Ticket ticket) {
        MessageTicket messageTicket = new MessageTicket();
        messageTicket.ticketId = ticket.getId();
        messageTicket.studyStatus = ticket.getStudyStatus();
        messageTicket.studyCategory = ticket.getStudyCategory();

        messageTicket.startTime = Long.toString(ticket.getStartTime().toEpochSecond(ZoneOffset.of("+09:00")));
        messageTicket.endTime = Long.toString(ticket.getEndTime().toEpochSecond(ZoneOffset.of("+09:00")));
        messageTicket.studyTime = Long.toString(ticket.getStudyTime());

        messageTicket.memberId = ticket.getMemberId();
        messageTicket.studyId = Long.toString(ticket.getStudy().getId());

        return messageTicket;
    }
}

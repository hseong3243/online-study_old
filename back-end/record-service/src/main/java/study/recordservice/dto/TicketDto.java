package study.recordservice.dto;

import study.recordservice.domain.StudyStatus;

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
}

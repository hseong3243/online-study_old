package study.studyservice.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    private String memberId;

    private int seat;
//    private LocalDateTime rootStartTime;
    private long studyTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;
    @Enumerated(EnumType.STRING)
    private StudyStatus studyStatus;
    @Enumerated(EnumType.STRING)
    private StudyCategory studyCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    public void setStudyTime(long studyTime) {
        this.studyTime = studyTime;
    }

    public void setInfo(Room room, int seat, Study study, StudyCategory studyCategory) {
        this.room = room;
        this.seat = seat;
        this.study = study;
        this.studyCategory= studyCategory;
    }

    public void validToExpire() {
        this.ticketStatus = TicketStatus.EXPIRE;
        this.endTime = LocalDateTime.now();
    }

    public static Ticket studyTicket(String memberId) {
        Ticket ticket = new Ticket();
        ticket.memberId = memberId;
        ticket.startTime = LocalDateTime.now();
        ticket.endTime = LocalDateTime.now();
        ticket.studyStatus = StudyStatus.STUDY;
        ticket.ticketStatus = TicketStatus.VALID;

        return ticket;
    }

    public static Ticket restTicket(String memberId) {
        Ticket ticket = new Ticket();
        ticket.memberId = memberId;
        ticket.startTime = LocalDateTime.now();
        ticket.endTime = LocalDateTime.now();
        ticket.studyStatus = StudyStatus.REST;
        ticket.ticketStatus = TicketStatus.VALID;

        return ticket;
    }
}

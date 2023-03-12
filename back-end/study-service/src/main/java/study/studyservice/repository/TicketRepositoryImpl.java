package study.studyservice.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import study.studyservice.domain.*;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static study.studyservice.domain.QRoom.*;
import static study.studyservice.domain.QStudy.*;
import static study.studyservice.domain.QTicket.*;

public class TicketRepositoryImpl implements TicketRepositoryCustom{

    private final EntityManager em;
    private final JPAQueryFactory query;

    public TicketRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<Ticket> findAllByMemberId(String memberId,
                                          TicketSearchCondition condition) {

        List<Ticket> result = query
                .select(ticket)
                .from(ticket)
                .where(ticket.memberId.eq(memberId),
                        ticket.ticketStatus.eq(condition.getStatus()))
                .fetch();

        return result;
    }

    @Override
    public Optional<Ticket> findByRoomAndSeat(Room room, int seat) {

        return Optional.ofNullable(query
                .select(ticket)
                .from(ticket)
                .where(ticket.room.eq(room),
                        ticket.seat.eq(seat),
                        ticket.ticketStatus.eq(TicketStatus.VALID))
                .fetchOne());
    }

    @Override
    public Page<Study> findStudiesByMemberId(String memberId, Pageable pageable) {
        List<Study> content = query
                .select(ticket.study).distinct()
                .from(ticket)
                .where(ticket.startTime.between(LocalDateTime.now().minusDays(7), LocalDateTime.now()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = query
                .select(ticket.study.countDistinct())
                .from(ticket)
                .where(ticket.startTime.between(LocalDateTime.now().minusDays(7), LocalDateTime.now()))
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public Page<Study> findStudiesRecent(String memberId, LocalDateTime date, Pageable pageable) {
        List<Study> result = query
                .select(study).distinct()
                .from(ticket).join(ticket.study, study)
                .where(memberIdEq(memberId),
                        ticket.startTime.after(date))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = query
                .select(study.name.countDistinct()).distinct()
                .from(ticket).join(ticket.study, study)
                .where(memberIdEq(memberId),
                        ticket.startTime.after(date))
                .fetchOne();

        if(count==null)
            count = 0L;

        return new PageImpl<>(result, pageable, count);
    }

    @Override
    public Long findRoomTicketCount(Room room) {
        Long count = query
                .select(ticket.id.count())
                .from(ticket).join(ticket.room, QRoom.room)
                .where(ticket.room.eq(room), ticket.ticketStatus.eq(TicketStatus.VALID))
                .fetchOne();
        return count;
    }


    private BooleanExpression memberIdEq(String memberId) {
        return memberId == null ? null : ticket.memberId.eq(memberId);
    }

}

package study.studyservice.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import study.studyservice.domain.QRoom;
import study.studyservice.domain.QTicket;
import study.studyservice.domain.Room;
import study.studyservice.domain.TicketStatus;

import javax.persistence.EntityManager;

import java.util.List;

import static study.studyservice.domain.QRoom.*;
import static study.studyservice.domain.QTicket.*;

public class RoomRepositoryImpl implements RoomRepositoryCustom {
    private final EntityManager em;
    private final JPAQueryFactory query;

    public RoomRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Page<Room> findRoomsByMembers(int members, Pageable pageable) {

        List<Room> result = query
                .select(room)
                .from(room)
                .leftJoin(room.tickets, ticket)
                .on(room.eq(ticket.room), ticket.ticketStatus.eq(TicketStatus.VALID))
                .where(room.groupId.notIn(0))
                .groupBy(room.id)
                .orderBy(ticket.ticketStatus.count().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = query
                .select(room.count())
                .from(room)
                .where(room.groupId.notIn(0))
                .fetchOne();

        if(count == null) {
            count = 0L;
        }

        return new PageImpl<>(result, pageable, count);
    }
}

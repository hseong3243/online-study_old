package study.studyservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.studyservice.domain.Room;
import study.studyservice.domain.Study;
import study.studyservice.domain.Ticket;
import study.studyservice.dto.TicketDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TicketRepositoryCustom {
    List<Ticket> findAllByMemberId(String memberId, TicketSearchCondition condition);

    Optional<Ticket> findByRoomAndSeat(Room room, int seat);

    Page<Study> findStudiesByMemberId(String memberId, Pageable pageable);

    Page<Study> findStudiesRecent(String memberId, LocalDateTime date, Pageable pageable);

    Long findRoomTicketCount(Room room);
}

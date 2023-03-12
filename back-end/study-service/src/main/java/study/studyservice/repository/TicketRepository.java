package study.studyservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.studyservice.domain.Room;
import study.studyservice.domain.Ticket;
import study.studyservice.domain.TicketStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long>, TicketRepositoryCustom {
    Ticket findByMemberId(String memberId);

    List<Ticket> findAllByMemberId(String memberId);

    Optional<Ticket> findByMemberIdAndTicketStatus(String memberId, TicketStatus status);

    @Query("select t from Ticket t where t.memberId = :memberId and t.ticketStatus in :statuses")
    Optional<Ticket> findByMemberIdAndTicketStatus(@Param("memberId") String memberId,
                                                   @Param("statuses") List<TicketStatus> statuses);

    List<Ticket> findByRoomAndTicketStatus(Room room, TicketStatus status);

    @Query("select t from Ticket t where t.room = :room and t.ticketStatus in :statuses")
    List<Ticket> findByRoomIdAndTicketStatus(@Param("room") Room room,
                                             @Param("statuses") List<TicketStatus> statuses);

    Page<Ticket> findAllByMemberId(String memberId, Pageable pageable);

    Page<Ticket> findAllByMemberIdAndStartTimeAfter(String memberId, LocalDateTime startTime, Pageable pageable);

    @Query("select t from Ticket t where t.room in :rooms and t.ticketStatus=:status")
    List<Ticket> findAllByRooms(@Param("rooms") List<Room> rooms, @Param("status") TicketStatus status);
}

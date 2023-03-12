package study.studyservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.studyservice.domain.Room;

public interface RoomRepositoryCustom {

    Page<Room> findRoomsByMembers(int members, Pageable pageable);
}

package study.studyservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.studyservice.domain.Room;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomRepositoryCustom {
    List<Room> findAllByGroupId(Long groupId);

    Page<Room> findAllByGroupId(Long groupId, Pageable pageable);

    Page<Room> findAllByGroupIdNotOrderByIdDesc(Long groupId, Pageable pageable);

    @Query(value = "select r from Room r where r.groupId in :groupIds")
    Page<Room> findAllByGroupIds(@Param("groupIds") List<Long> groupIds, Pageable pageable);
}

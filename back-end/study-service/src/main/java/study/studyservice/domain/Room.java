package study.studyservice.domain;

import lombok.Getter;
import study.studyservice.dto.RoomDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    private String name;
    private String description;

    private Long groupId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "room")
    private List<Ticket> tickets = new ArrayList<>();

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public static Room create(RoomDto roomDto) {
        Room room = new Room();
        room.name = roomDto.getName();
        room.description = room.getDescription();

        return room;
    }

    public static Room createGroupRoom(String name, Long groupId){
        Room room = new Room();
        room.name = name;
        room.groupId = groupId;
        return room;
    }
}

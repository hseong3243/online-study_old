package study.studyservice.dto;

import lombok.Data;
import study.studyservice.domain.Room;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class RoomDto {

    private Long roomId;
    @NotNull(message = "RoomName cannot be null")
    @Size(min = 2)
    private String name;
    private String description;
    private Long groupId;
    private int count;
    private GroupDto groupDto;
    private List<TicketDto> ticketDtos = new ArrayList<>();

    public static RoomDto from(Room room) {
        RoomDto roomDto = new RoomDto();
        roomDto.roomId = room.getId();
        roomDto.name = room.getName();
        roomDto.groupId = room.getGroupId();
        roomDto.description = room.getDescription();
        return roomDto;
    }
}

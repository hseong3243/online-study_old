package study.groupservice.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoomDto {

    private Long roomId;
    private String name;
    private String description;
    private Long groupId;
    private int count;
    private List<TicketDto> ticketDtos = new ArrayList<>();
}

package study.studyservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.studyservice.dto.Result;
import study.studyservice.dto.RoomDto;
import study.studyservice.service.RoomService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<RoomDto> getRoom(@PathVariable Long roomId) {
        RoomDto roomDto = roomService.findRoom(roomId);

        return ResponseEntity.status(OK).body(roomDto);
    }

    @GetMapping("/rooms")
    public ResponseEntity<Result<List<RoomDto>>> getRooms(@RequestParam(required = false) List<Long> groupIds,
                                                          @RequestParam(required = false) Long groupId,
                                                          @RequestParam(required = false) Integer page,
                                                          @RequestParam(required = false) Integer size) {

        Result<List<RoomDto>> result;
        if(groupIds != null) {
            Page<RoomDto> roomDtos = roomService.findAllRoomsByGroupIds(groupIds, page, size);
            result = new Result<>(roomDtos.getContent(), roomDtos.getNumber(), roomDtos.getTotalPages());
            result.setHasNext(roomDtos.hasNext());
            result.setHasPrevious(roomDtos.hasPrevious());
        }
        else if(page != null) {
            Page<RoomDto> roomDtos = roomService.findAllRooms(groupId, page, size);
            result = new Result<>(roomDtos.getContent(), roomDtos.getNumber(), roomDtos.getTotalPages());
            result.setHasNext(roomDtos.hasNext());
            result.setHasPrevious(roomDtos.hasPrevious());
        }else {
            List<RoomDto> roomDtos = roomService.findAllRooms(groupId);
            result = new Result<>(roomDtos, 0, 0);
        }

        return ResponseEntity.status(OK).body(result);
    }

    @GetMapping("/rooms/groups")
    public ResponseEntity<Result<List<RoomDto>>> getGroupRooms(@RequestParam(required = false) Integer members,
                                                               @RequestParam int page, @RequestParam int size) {
        Page<RoomDto> roomDtos;
        if(members==null)
            roomDtos = roomService.findAllGroupRooms(page, size);
        else
            roomDtos = roomService.findRoomsOrderByMembers(members, page, size);
        Result<List<RoomDto>> result = new Result<>(roomDtos.getContent(), roomDtos.getNumber(), roomDtos.getTotalPages());
        result.setHasNext(roomDtos.hasNext());
        result.setHasPrevious(roomDtos.hasPrevious());

        return ResponseEntity.status(OK).body(result);
    }

    @PostMapping("/rooms")
    public ResponseEntity<Long> creatRoom(@RequestBody RoomDto roomDto) {
        Long roomId = roomService.createRoom(roomDto);

        return ResponseEntity.status(CREATED).body(roomId);
    }
}

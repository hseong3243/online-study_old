package study.studyservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import study.studyservice.client.GroupClientService;
import study.studyservice.dto.GroupDto;
import study.studyservice.client.MemberServiceClient;
import study.studyservice.domain.Room;
import study.studyservice.domain.Ticket;
import study.studyservice.domain.TicketStatus;
import study.studyservice.dto.MemberDto;
import study.studyservice.dto.RoomDto;
import study.studyservice.dto.TicketDto;
import study.studyservice.repository.RoomRepository;
import study.studyservice.repository.TicketRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {

    private final RoomRepository roomRepository;
    private final TicketRepository ticketRepository;
    private final MemberServiceClient memberServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final GroupClientService groupClientService;

    public Long createRoom(RoomDto roomDto) {
        Room room = Room.create(roomDto);

        if(room.getGroupId() == null) {
            room.setGroupId(0L);
        }

        roomRepository.save(room);

        return room.getId();
    }

    public RoomDto findRoom(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> {
            throw new RuntimeException("잘못된 접근입니다.");
        });

        RoomDto roomDto = RoomDto.from(room);
        List<Ticket> tickets =
                ticketRepository.findByRoomAndTicketStatus(room, TicketStatus.VALID);

        List<String> memberIds = new ArrayList<>();
        tickets.forEach(ticket -> {
            memberIds.add(ticket.getMemberId());
        });

        // 멤버 목록 조회
        CircuitBreaker circuitbreaker = circuitBreakerFactory.create("circuitbreaker");
        log.info("Before call member-service");
        List<MemberDto> memberDtos = circuitbreaker.run(() -> memberServiceClient.getMembers(memberIds),
                throwable -> new ArrayList<>());
        log.info("After call member-service");

        // 티켓의 멤버 ID와 비교, 티켓 Dto 생성 후 멤버 이름 set
        List<TicketDto> ticketDtos = new ArrayList<>();
        tickets.forEach(ticket -> {
            MemberDto memberDto = memberDtos.stream()
                    .filter(member -> member.getMemberId().equals(ticket.getMemberId()))
                    .findFirst().get();
            TicketDto ticketDto = TicketDto.from(ticket);
            ticketDto.setMemberName(memberDto.getName());
            ticketDtos.add(ticketDto);
        });

        roomDto.setTicketDtos(ticketDtos);
        return roomDto;
    }

    public List<RoomDto> findAllRooms() {
        List<Room> rooms = roomRepository.findAll();

        return getRoomDtos(rooms);
    }


    public List<RoomDto> findAllRooms(Long groupId) {
        List<Room> rooms = roomRepository.findAllByGroupId(groupId);

        return getRoomDtos(rooms);
    }

    private List<RoomDto> getRoomDtos(List<Room> rooms) {
        List<RoomDto> roomDtos = new ArrayList<>();

        for (Room room: rooms) {
            List<Ticket> tickets = ticketRepository.findByRoomAndTicketStatus(room, TicketStatus.VALID);
            List<TicketDto> ticketDtos = tickets.stream().map(TicketDto::from).collect(Collectors.toList());
            RoomDto roomDto = RoomDto.from(room);
            roomDto.setTicketDtos(ticketDtos);

            roomDtos.add(roomDto);
        }

        return roomDtos;
    }

    public Page<RoomDto> findAllRooms(Long groupId, int page, int size) {
        PageRequest request = PageRequest.of(page, size);
        Page<Room> rooms = roomRepository.findAllByGroupId(groupId, request);

        Page<RoomDto> result = rooms.map(room -> {
            RoomDto roomDto = RoomDto.from(room);
            List<Ticket> tickets = ticketRepository.findByRoomAndTicketStatus(room, TicketStatus.VALID);
            List<TicketDto> ticketDtos = tickets.stream().map(TicketDto::from).collect(Collectors.toList());
            roomDto.setTicketDtos(ticketDtos);
            return roomDto;
        });

        return result;
    }

    public Page<RoomDto> findAllGroupRooms(int page, int size) {
        PageRequest request = PageRequest.of(page, size);

        Page<Room> rooms = roomRepository.findAllByGroupIdNotOrderByIdDesc(0L, request);

        Set<Long> set = new HashSet<>();
        Page<RoomDto> result = rooms.map(room -> {
            RoomDto roomDto = RoomDto.from(room);
            List<Ticket> tickets = ticketRepository.findByRoomAndTicketStatus(room, TicketStatus.VALID);
            List<TicketDto> ticketDtos = tickets.stream().map(TicketDto::from).collect(Collectors.toList());
            roomDto.setTicketDtos(ticketDtos);
            set.add(room.getGroupId());
            return roomDto;
        });

        List<Long> groupIds = new ArrayList<>(set);
        Map<Long, GroupDto> map = new HashMap<>();

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        for (Long groupId: groupIds) {
            log.info("Before call group-service");
            GroupDto groupDto = circuitBreaker.run(() -> groupClientService.getGroup(groupId), throwable -> new GroupDto());
            log.info("After call group-service");
            map.put(groupId, groupDto);
        }

        Page<RoomDto> roomDtos = result.map(roomDto -> {
            GroupDto groupDto = map.get(roomDto.getGroupId());
            roomDto.setGroupDto(groupDto);
            Room room = roomRepository.findById(roomDto.getRoomId()).get();
            List<Ticket> tickets = ticketRepository.findByRoomAndTicketStatus(room, TicketStatus.VALID);
            List<TicketDto> ticketDtos = tickets.stream().map(TicketDto::from).collect(Collectors.toList());
            roomDto.setTicketDtos(ticketDtos);
            return roomDto;
        });

        return roomDtos;

    }

    public Page<RoomDto> findAllRoomsByGroupIds(List<Long> groupIds, Integer page, Integer size) {
        PageRequest request = PageRequest.of(page, size);
        Page<Room> rooms = roomRepository.findAllByGroupIds(groupIds, request);

        List<Ticket> tickets = ticketRepository.findAllByRooms(rooms.getContent(), TicketStatus.VALID);

        List<TicketDto> ticketDtos = tickets.stream().map(TicketDto::from).collect(Collectors.toList());

        Page<RoomDto> roomDtos = rooms.map(RoomDto::from);

        Page<RoomDto> result = roomDtos.map(roomDto -> {
            roomDto.setTicketDtos(ticketDtos.stream()
                    .filter(ticketDto -> ticketDto.getRoomName().equals(roomDto.getName()))
                    .collect(Collectors.toList()));
            roomDto.setCount(roomDto.getTicketDtos().size());
            return roomDto;
        });

        return result;
    }

    public Page<RoomDto> findRoomsOrderByMembers(int members, int page, int size) {
        PageRequest request = PageRequest.of(page, size);
        Page<Room> rooms = roomRepository.findRoomsByMembers(members, request);

        Set<Long> set = new HashSet<>();
        Page<RoomDto> result = rooms.map(room -> {
            RoomDto roomDto = RoomDto.from(room);
            List<Ticket> tickets = ticketRepository.findByRoomAndTicketStatus(room, TicketStatus.VALID);
            List<TicketDto> ticketDtos = tickets.stream().map(TicketDto::from).collect(Collectors.toList());
            roomDto.setTicketDtos(ticketDtos);
            set.add(room.getGroupId());
            return roomDto;
        });

        List<Long> groupIds = new ArrayList<>(set);
        Map<Long, GroupDto> map = new HashMap<>();

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        for (Long groupId: groupIds) {
            log.info("Before call group-service");
            GroupDto groupDto = circuitBreaker.run(() -> groupClientService.getGroup(groupId), throwable -> new GroupDto());
            log.info("After call group-service");
            map.put(groupId, groupDto);
        }

        Page<RoomDto> roomDtos = result.map(roomDto -> {
            GroupDto groupDto = map.get(roomDto.getGroupId());
            roomDto.setGroupDto(groupDto);
            Room room = roomRepository.findById(roomDto.getRoomId()).get();
            List<Ticket> tickets = ticketRepository.findByRoomAndTicketStatus(room, TicketStatus.VALID);
            List<TicketDto> ticketDtos = tickets.stream().map(TicketDto::from).collect(Collectors.toList());
            roomDto.setTicketDtos(ticketDtos);
            return roomDto;
        });

        return roomDtos;
    }
}

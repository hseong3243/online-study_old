package study.studyservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.studyservice.client.MemberServiceClient;
import study.studyservice.domain.*;
import study.studyservice.dto.MemberDto;
import study.studyservice.dto.StudyDto;
import study.studyservice.dto.TicketDto;
import study.studyservice.dto.TicketParam;
import study.studyservice.messagequeue.KafkaProducer;
import study.studyservice.messagequeue.MessageTicket;
import study.studyservice.repository.RoomRepository;
import study.studyservice.repository.StudyRepository;
import study.studyservice.repository.TicketRepository;
import study.studyservice.repository.TicketSearchCondition;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
//@RequiredArgsConstructor
public class TicketService {

    TicketRepository ticketRepository;
    RoomRepository roomRepository;
    StudyRepository studyRepository;
    MemberServiceClient memberServiceClient;
    CircuitBreakerFactory circuitBreakerFactory;
    KafkaProducer kafkaProducer;
    CircuitBreaker circuitBreaker;

    public TicketService(TicketRepository ticketRepository, RoomRepository roomRepository, StudyRepository studyRepository,
                         MemberServiceClient memberServiceClient, CircuitBreakerFactory circuitBreakerFactory, KafkaProducer kafkaProducer) {
        this.ticketRepository = ticketRepository;
        this.roomRepository = roomRepository;
        this.studyRepository = studyRepository;
        this.memberServiceClient = memberServiceClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
        this.kafkaProducer = kafkaProducer;
        this.circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
    }

    public Long issueTicket(TicketParam ticketParam) {
        Room room = roomRepository.findById(ticketParam.getRoomId()).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 방입니다.");
        });

        // 유효한 티켓 발급 여부 검증
        ticketRepository.findByMemberIdAndTicketStatus(ticketParam.getMemberId(), TicketStatus.VALID).ifPresent((ticket) -> {
            throw new RuntimeException("이미 유효한 티켓이 존재합니다");
        });
        // 다른 사람 티켓 발급 여부 검증
        ticketRepository.findByRoomAndSeat(room, ticketParam.getSeat()).ifPresent(ticket -> {
            throw new RuntimeException("해당 자리의 회원이 이미 존재합니다.");
        });
        Study study = studyRepository.findById(ticketParam.getStudyId()).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 스터디입니다.");
        });

        if(ticketRepository.findRoomTicketCount(room)>=28) {
            throw new RuntimeException("더 이상 티켓을 발급받을 수 없습니다.");
        }

//        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        log.info("Before call member-service");
        MemberDto memberDto = circuitBreaker.run(() -> memberServiceClient.getMember(ticketParam.getMemberId()),
                throwable -> new MemberDto());
        log.info("After call member-service");

        // 티켓 생성, 루트 티켓 지정, 방과 자리, 스터디 지정
        Ticket ticket = Ticket.studyTicket(ticketParam.getMemberId());
        ticket.setInfo(room, ticketParam.getSeat(), study, memberDto.getStudyCategory());
        ticket.setStudyTime(0);
        ticketRepository.save(ticket);
        log.info("티켓이 발행되었습니다. memberId={}, roomId={}, seat={}", ticketParam.getMemberId(), ticketParam.getRoomId(), ticketParam.getRoomId());

        kafkaProducer.send("study-ticket-issue", MessageTicket.from(ticket));

        return ticket.getId();
    }

    public TicketDto getTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 티켓입니다.");
        });

        log.info("Before call member-service");
        MemberDto memberDto = circuitBreaker.run(() -> memberServiceClient.getMember(ticket.getMemberId()),
                throwable -> new MemberDto());
        log.info("After call member-service");

        TicketDto ticketDto = TicketDto.from(ticket);
        ticketDto.setMemberName(memberDto.getName());

        return ticketDto;
    }

    public Page<StudyDto> getStudiesByMemberId(String memberId) {
        PageRequest request = PageRequest.of(0, 8);
        Page<Study> studies = ticketRepository.findStudiesByMemberId(memberId, request);
        Page<StudyDto> studyDtos = studies.map(StudyDto::from);

        return studyDtos;
    }

    public List<TicketDto> getTicketsByMemberId(String memberId, TicketSearchCondition condition) {
        List<Ticket> result = ticketRepository.findAllByMemberId(memberId, condition);
        List<TicketDto> ticketDtos = new ArrayList<>();
        result.forEach(ticket -> {
            ticketDtos.add(TicketDto.from(ticket));
        });

        return ticketDtos;
    }

    public TicketDto getValidTicket(String memberId) {
        Ticket ticket = ticketRepository.findByMemberIdAndTicketStatus(memberId, TicketStatus.VALID)
                .orElse(new Ticket());

        TicketDto ticketDto;
        if(ticket.getId() != null)
            ticketDto = TicketDto.from(ticket);
        else
            ticketDto = new TicketDto();

//        CircuitBreaker circuitbreaker = circuitBreakerFactory.create("circuitbreaker");
        log.info("Before call member-service");
        MemberDto memberDto = circuitBreaker.run(() -> memberServiceClient.getMember(memberId),
                throwable -> new MemberDto());
        log.info("After call member-service");

        Optional.ofNullable(memberDto.getName()).ifPresent(ticketDto::setMemberName);

        return ticketDto;
    }

    public Long changeStudyStatus(Long ticketId, String studyStatus) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> {
            throw new RuntimeException("유효한 티켓이 존재하지 않습니다.");
        });

        Ticket issuedTicket;
        switch (studyStatus) {
            case "STUDY":
                if(ticket.getStudyStatus() != StudyStatus.REST)
                    throw new RuntimeException("잘못된 접근입니다.");
                issuedTicket = Ticket.studyTicket(ticket.getMemberId());
                issuedTicket.setStudyTime(ticket.getStudyTime());
                break;
            case "REST":
                if(ticket.getStudyStatus() != StudyStatus.STUDY)
                    throw new RuntimeException("잘못된 접근입니다.");
                issuedTicket = Ticket.restTicket(ticket.getMemberId());
                long startTime = ticket.getStartTime().toEpochSecond(ZoneOffset.of("+09:00"));
                long endTime = ticket.getEndTime().toEpochSecond(ZoneOffset.of("+09:00"));
                issuedTicket.setStudyTime(ticket.getStudyTime() + endTime-startTime);
                break;
            default:
                throw new RuntimeException("잘못된 접근입니다.");
        }
        // 기존 티켓 만료 -> 이벤트 발행
        ticket.validToExpire();
        switch (studyStatus) {
            case "STUDY":
                issuedTicket = Ticket.studyTicket(ticket.getMemberId());
                issuedTicket.setStudyTime(ticket.getStudyTime());
                break;
            case "REST":
                issuedTicket = Ticket.restTicket(ticket.getMemberId());
                long startTime = ticket.getStartTime().toEpochSecond(ZoneOffset.of("+09:00"));
                long endTime = ticket.getEndTime().toEpochSecond(ZoneOffset.of("+09:00"));
                issuedTicket.setStudyTime(ticket.getStudyTime() + endTime - startTime);
        }
        MessageTicket messageTicket = MessageTicket.from(ticket);
        kafkaProducer.send("study-ticket-expire", messageTicket);

        // 새로운 티켓 정보 설정 후 저장
        issuedTicket.setInfo(ticket.getRoom(), ticket.getSeat(), ticket.getStudy(), ticket.getStudyCategory());
        ticketRepository.save(issuedTicket);

        return issuedTicket.getId();
    }

    public Long expireTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> {
            throw new RuntimeException("유효한 티켓이 존재하지 않습니다.");
        });

        // 티켓 만료 -> 이벤트 발행
        ticket.validToExpire();
//        long startTime = ticket.getStartTime().toEpochSecond(ZoneOffset.of("+09:00"));
//        long endTime = ticket.getEndTime().toEpochSecond(ZoneOffset.of("+09:00"));
        MessageTicket messageTicket = MessageTicket.from(ticket);
        kafkaProducer.send("study-ticket-expire", messageTicket);

        return ticketId;
    }

    public List<StudyDto> getStudies(List<Long> studyIds) {
        List<Study> studies = studyRepository.findAllById(studyIds);
        List<StudyDto> studyDtos = studies.stream().map(StudyDto::from).collect(Collectors.toList());

        return studyDtos;
    }

    public Page<StudyDto> getStudiesByMemberId(String memberId, Integer day) {
        LocalDateTime date = LocalDateTime.now().minusDays(day);

        PageRequest request = PageRequest.of(0, 10);
        Page<Study> studies = ticketRepository.findStudiesRecent(memberId, date, request);

        return studies.map(StudyDto::from);
    }
}

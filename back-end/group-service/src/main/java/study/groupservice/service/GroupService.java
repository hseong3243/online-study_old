package study.groupservice.service;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.groupservice.client.*;
import study.groupservice.domain.*;
import study.groupservice.dto.*;
import study.groupservice.messagequeue.KafkaProducer;
import study.groupservice.messagequeue.MessageGroup;
import study.groupservice.params.GroupParams;
import study.groupservice.params.PurposeParam;
import study.groupservice.repository.GroupMemberRepository;
import study.groupservice.repository.GroupPurposeRepository;
import study.groupservice.repository.GroupRepository;
import study.groupservice.repository.PurposeRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GroupService {

    GroupRepository groupRepository;
    GroupMemberRepository groupMemberRepository;
    MemberServiceClient memberServiceClient;
    RecordServiceClient recordServiceClient;
    StudyServiceClient studyServiceClient;
    PurposeRepository purposeRepository;
    GroupPurposeRepository groupPurposeRepository;
    KafkaProducer kafkaProducer;
    CircuitBreakerFactory circuitBreakerFactory;
    CircuitBreaker circuitBreaker;
    private Page<Group> groups;

    public GroupService(GroupRepository groupRepository, GroupMemberRepository groupMemberRepository,
                        RecordServiceClient recordServiceClient, StudyServiceClient studyServiceClient,
                        PurposeRepository purposeRepository, MemberServiceClient memberServiceClient,
                        GroupPurposeRepository groupPurposeRepository, KafkaProducer kafkaProducer,
                        CircuitBreakerFactory circuitBreakerFactory) {
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.memberServiceClient = memberServiceClient;
        this.recordServiceClient = recordServiceClient;
        this.studyServiceClient = studyServiceClient;
        this.purposeRepository = purposeRepository;
        this.groupPurposeRepository = groupPurposeRepository;
        this.kafkaProducer = kafkaProducer;
        this.circuitBreakerFactory = circuitBreakerFactory;
        this.circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
    }

    public Page<GroupDto> getGroups(int page, int size, String order, String search) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Group> groups;
        if(order==null) {
            groups = groupRepository.findAllOrderByCreated(search,null, pageRequest);
        } else if(order.equals("members")) {
            groups = groupRepository.findGroupsOrderByMembers(search, null, pageRequest);
        }
        else if(order.equals("attend")) {
            LocalDateTime start = LocalDate.now().atStartOfDay().plusHours(5);
            if(LocalDateTime.now().getHour()<5)
                start = start.minusDays(1);
            groups = groupRepository.findGroupsOrderByAttend(search, null, start, pageRequest);
        }
        else
            groups = groupRepository.findAll(pageRequest);

        return getGroupDtos(groups);
    }

    public Page<GroupDto> getGroupsByGroupCategory(GroupCategory groupCategory, int page, int size, String order, String search) {
        PageRequest request = PageRequest.of(page, size);
        Page<Group> groups;
        if(order==null)
            groups = groupRepository.findAllOrderByCreated(search, groupCategory, request);
        else if(order.equals("members"))
            groups = groupRepository.findGroupsOrderByMembers(search, groupCategory, request);
        else if(order.equals("attend")){
            LocalDateTime start = LocalDate.now().atStartOfDay().plusHours(5);
            if(LocalDateTime.now().getHour()<5)
                start = start.minusDays(1);
            groups = groupRepository.findGroupsOrderByAttend(search, groupCategory, start, request);
        }
        else
            groups = groupRepository.findAllByGroupCategory(groupCategory, request);
        return getGroupDtos(groups);
    }

    public Page<GroupDto> getGroups(String memberId, int page, int size, String order, String search) {
        PageRequest request = PageRequest.of(page, size);
        Page<Group> groups = groupRepository.findAllByMemberId(search, memberId, request);
        List<Long> studyIds = new ArrayList<>();
        List<Long> groupIds = new ArrayList<>();
        groups.map(group -> {
            studyIds.add(group.getStudyId());
            groupIds.add(group.getId());
            return null;
        });

        log.info("Before call study-service");
        Result<List<StudyDto>> result = circuitBreaker.run(() -> studyServiceClient.getStudies(studyIds),
                throwable -> new Result<>());
        log.info("After call study-service");
        log.info("Before call study-service");
        Result<List<RoomDto>> roomResult
                = circuitBreaker.run(() -> studyServiceClient.getRooms(groupIds, page, size * 2), throwable -> new Result<>());
        log.info("After call study-service");

        List<StudyDto> studyDtos = result.getData();
        List<RoomDto> roomDtos = roomResult.getData();

        Map<Long, StudyDto> idToStudy = studyDtos.stream().collect(Collectors.toMap(StudyDto::getStudyId, studyDto -> studyDto));

        Page<GroupDto> groupDtos = groups.map(group -> {
            GroupDto groupDto = GroupDto.from(group);
            groupDto.setStudy(idToStudy.get(group.getStudyId()));
            groupDto.setRooms(roomDtos.stream().filter(room -> room.getGroupId().equals(group.getId())).collect(Collectors.toList()));
            groupDto.setMembers(group.getGroupMembers().stream().map(GroupMemberDto::from).collect(Collectors.toList()));
            return groupDto;
        });

        return groupDtos;
    }

    @NotNull
    private Page<GroupDto> getGroupDtos(Page<Group> groups) {
        List<Long> studyIds = new ArrayList<>();

        groups.getContent().forEach(group -> {
            studyIds.add(group.getStudyId());
        });

        log.info("Before call study-service");
        Result<List<StudyDto>> result = circuitBreaker.run(() -> studyServiceClient.getStudies(studyIds),
                throwable -> new Result<>());
        log.info("After call study-service");
        List<StudyDto> studies = result.getData();

        Map<Long, StudyDto> studyMap = studies.stream().collect(Collectors.toMap(StudyDto::getStudyId, studyDto -> studyDto));

        Page<GroupDto> groupDtos = groups.map(group -> {
            GroupDto groupDto = GroupDto.from(group);
            List<GroupMemberDto> groupMemberDtos
                    = group.getGroupMembers().stream().map(GroupMemberDto::from).collect(Collectors.toList());
            groupDto.setMembers(groupMemberDtos);
            groupDto.setStudy(studyMap.get(group.getStudyId()));
            return groupDto;
        });


        return groupDtos;
    }

    @Transactional
    public Long createGroup(GroupParams groupParams, String memberId) {
        Group group = Group.create(groupParams, memberId);
        groupRepository.save(group);

        GroupMember groupMember = GroupMember.create(group, memberId);
        groupMember.upgradeRole();
        groupMemberRepository.save(groupMember);

        // 그룹 생성 이벤트 발행
        MessageGroup messageGroup = MessageGroup.from(group);
        kafkaProducer.send("group-group-create", messageGroup);

        return group.getId();
    }

    public GroupDto getGroup(Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 그룹입니다.");
        });

        GroupDto groupDto = GroupDto.from(group);

        List<GroupMember> groupMembers = group.getGroupMembers();
        List<String> memberIds = groupMembers.stream().map(GroupMember::getMemberId).collect(Collectors.toList());
        log.info("Before call member-service");
        List<MemberDto> memberDtos =
                circuitBreaker.run(() -> memberServiceClient.getMembers(memberIds), throwable -> new ArrayList<>());
        log.info("Before call member-service");
        Map<String, String> idToMap
                = memberDtos.stream().collect(Collectors.toMap(MemberDto::getMemberId, MemberDto::getName));

        List<GroupMemberDto> groupMemberDtos = group.getGroupMembers().stream().map(groupMember -> {
            GroupMemberDto groupMemberDto = GroupMemberDto.from(groupMember);
            groupMemberDto.setMemberName(idToMap.get(groupMember.getMemberId()));
            return groupMemberDto;
        }).collect(Collectors.toList());

        groupDto.setMembers(groupMemberDtos);

        log.info("Before call study-service");
        Result<List<RoomDto>> rooms = circuitBreaker.run(() -> studyServiceClient.getRooms(groupId),
                throwable -> new Result<>());
        log.info("After call study-service");
        log.info("Before call study-service");
        StudyDto studyDto = circuitBreaker.run(() -> studyServiceClient.getStudy(group.getStudyId()),
                throwable -> new StudyDto());
        log.info("After call study-service");
        groupDto.setRooms(rooms.getData());
        groupDto.setStudy(studyDto);

        return groupDto;
    }

    @Transactional
    public Long updateIntro(Long groupId, GroupParams groupParams, String memberId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 그룹입니다.");
        });

        if(!group.getMemberId().equals(memberId)) {
            throw new RuntimeException("허용되지 않는 접근입니다");
        }

        log.info("그룹 수정: {}", groupParams.toString());

        group.changeIntro(groupParams.getIntro());

        return group.getId();
    }

    @Transactional
    public void deleteGroup(Long groupId, String memberId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 그룹입니다.");
        });

        if(!group.getMemberId().equals(memberId)) {
            throw new RuntimeException("허용되지 않는 접근입니다.");
        }
        groupRepository.delete(group);

    }

    @Transactional
    public Long joinGroup(Long groupId, String memberId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 그룹입니다.");
        });

        if(group.getGroupMembers().size() >= group.getLevel()*56)
            throw new RuntimeException("그룹이 가득찼습니다.");

        groupMemberRepository.findByGroupAndAndMemberId(group, memberId)
                .ifPresent(groupMember -> {throw new RuntimeException("이미 해당 그룹에 소속되어 있습니다.");});

        GroupMember groupMember = GroupMember.create(group, memberId);

        groupMemberRepository.save(groupMember);

        return groupMember.getId();
    }

    @Transactional
    public void exitGroup(Long groupId, String memberId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 그룹입니다.");
        });
        GroupMember groupMember = groupMemberRepository.findByGroupAndAndMemberId(group, memberId).orElseThrow(() -> {
            throw new RuntimeException("해당 그룹에 속한 멤버가 아닙니다.");
        });

        groupMemberRepository.delete(groupMember);
    }

    @Transactional
    public List<String> getGroupMembers(Long groupId, String memberId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 그룹입니다.");
        });

        groupMemberRepository.findByGroupAndAndMemberId(group, memberId).orElseThrow(() -> {
            throw new RuntimeException("그룹 멤버만 이용가능합니다.");
        });

        List<GroupMember> groupMembers = groupMemberRepository.findByGroup(group);

        List<String> members = new ArrayList<>();
        groupMembers.forEach(groupMember -> {
            members.add(groupMember.getMemberId());
        });

        return members;


    }

    @Transactional
    public Long addPurpose(PurposeParam purposeParam, Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 그룹입니다.");
        });

        Purpose purpose = purposeRepository.findByContent(purposeParam.getContent()).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 퍼포즈입니다.");
        });

        groupPurposeRepository.findByGroupAndPurpose(group, purpose).ifPresent((groupPurpose) -> {
            throw new RuntimeException("동일한 퍼포즈를 추가할 수 없습니다.");
        });

        GroupPurpose groupPurpose = new GroupPurpose();
        groupPurpose.setGroupAndPurpose(group, purpose);

        groupPurposeRepository.save(groupPurpose);

        return groupPurpose.getId();
    }

    public void removePurpose(PurposeParam purposeParam, Long groupId, String memberId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> {
            throw new RuntimeException("존재하지 않는 그룹입니다.");
        });

        if(!group.getMemberId().equals(memberId))
            throw new RuntimeException("권한이 없습니다.");

        GroupPurpose groupPurpose = groupPurposeRepository.findById(purposeParam.getGroupPurposeId()).orElseThrow(() -> {
            throw new RuntimeException("잘못된 접근입니다.");
        });

        groupPurposeRepository.delete(groupPurpose);
    }

    public Long createPurpose(PurposeParam purposeParam) {
        purposeRepository.findByContent(purposeParam.getContent()).ifPresent(purpose -> {
            throw new RuntimeException("이미 존재하는 퍼포즈입니다.");
        });

        Purpose purpose = Purpose.create(purposeParam);
        purposeRepository.save(purpose);

        return purpose.getId();
    }

    public List<PurposeDto> getPurposes(String content) {
        List<Purpose> purposes;
        if(content != null)
            purposes = purposeRepository.findByContentContaining(content);
        else
            purposes = purposeRepository.findAll();
        List<PurposeDto> purposeDtos = purposes.stream().map(PurposeDto::from).collect(Collectors.toList());

        return purposeDtos;
    }
}

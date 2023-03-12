package study.recordservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import study.recordservice.client.*;
import study.recordservice.domain.StudyCategory;
import study.recordservice.domain.StudyStatus;
import study.recordservice.domain.Record;
import study.recordservice.dto.*;
import study.recordservice.repository.RecordRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RecordService {

    private final RecordRepository recordRepository;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final StudyServiceClient studyServiceClient;
    private final CircuitBreaker circuitBreaker;
    private final MemberServiceClient memberServiceClient;
    private final GroupClientService groupClientService;

    public RecordService(RecordRepository recordRepository, CircuitBreakerFactory circuitBreakerFactory,
                         StudyServiceClient studyServiceClient, MemberServiceClient memberServiceClient, GroupClientService groupClientService) {
        this.recordRepository = recordRepository;
        this.circuitBreakerFactory = circuitBreakerFactory;
        this.studyServiceClient = studyServiceClient;
        this.circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        this.memberServiceClient = memberServiceClient;
        this.groupClientService = groupClientService;
    }

    public Long createRecord(RecordParam recordParam) {
        int startMin = 21323*recordParam.getTestCode();
//        int startMax = 30000;
        int endMin = 35360*recordParam.getTestCode();
//        int endMax = 75000;
//        long start = (long)(Math.random()*(startMax-startMin) + startMin);
//        long end = (long) (Math.random() * (endMax - endMin) + endMin);
        Record record = new Record(StudyStatus.STUDY, StudyCategory.UNIV, startMin, endMin,
                recordParam.getDate(), recordParam.getMemberId(), recordParam.getStudyId());
        recordRepository.save(record);

        return record.getId();
    }

    public StudyRecord getRecords(String memberId, String period, LocalDate date, Long studyId) {
        LocalDate start;
        LocalDate end = date;
        start = getStartDate(period, date);

        List<RecordDto> memberRecords = recordRepository.findAllRecord(memberId, studyId, start, end);
        List<RecordDto> result = memberRecords.stream()
                .filter(record -> record.getStudyStatus().equals(StudyStatus.STUDY))
                .collect(Collectors.toList());

        Set<String> counter = new HashSet<>();
        long memberStudyTime = 0;
        if(result.size() != 0) {
            List<RecordDto> studyResult = new ArrayList<>();
            long studyTime = 0;
            long startTime = result.get(0).getStartTime();
            LocalDate beforeDate = result.get(0).getDate();

            for(int i=0; i<result.size(); i++) {
                if (result.get(i).getDate().equals(beforeDate)) {
                    studyTime += result.get(i).getEndTime() - result.get(i).getStartTime();
                } else {
                    RecordDto studyRecord = RecordDto.createStudyRecord(studyId, startTime, startTime + studyTime, beforeDate);
                    studyResult.add(studyRecord);
                    beforeDate = beforeDate.plusDays(1);
                    studyTime = result.get(i).getEndTime() - result.get(i).getStartTime();
                    startTime = result.get(i).getStartTime();
                }

                if(result.get(i).getDate().equals(beforeDate) && result.size()-1 == i) {
                    RecordDto studyRecord = RecordDto.createStudyRecord(studyId, startTime, startTime + studyTime, beforeDate);
                    studyResult.add(studyRecord);
                    beforeDate = beforeDate.plusDays(1);
                    studyTime = result.get(i).getEndTime() - result.get(i).getStartTime();
                    startTime = result.get(i).getStartTime();
                }

                memberStudyTime += result.get(i).getEndTime() - result.get(i).getStartTime();
            }

            result = studyResult;
        }

        log.info("Before call study-service");
        Result<List<StudyDto>> studyResult = circuitBreaker.run(() -> studyServiceClient.getStudies(List.of(studyId)),
                throwable -> new Result<>());
        log.info("After call study-service");

        List<StudyDto> studies = studyResult.getData();

        return StudyRecord.createMemberStudyRecords(studies, memberId, memberStudyTime, result);

    }

    public StudyRecord getRecords(String memberId, String period, LocalDate date) {
        LocalDate start = getStartDate(period, date);

        List<RecordDto> records = recordRepository.findAllRecord(memberId, null, start, date);
        long startTimeAvg=0;
        long endTimeAvg=0;
        long studyTime=0;
        long restTime=0;

        ZoneOffset zone = ZoneOffset.of("+09:00");

        long beforeStartTime = LocalDateTime.now().toEpochSecond(zone);
        long beforeEndTime = 0;

        LocalDate before = date;
        Set<Long> studyIdSet = new HashSet<>();
        int days = 0;
        for (RecordDto record: records) {
            if(before.isBefore(record.getDate())) {
                startTimeAvg += beforeStartTime;
                endTimeAvg += beforeEndTime;
                studyTime += beforeEndTime - beforeStartTime;
                days++;

                beforeStartTime = LocalDateTime.now().toEpochSecond(zone);
                beforeEndTime = 0;

            }

            studyIdSet.add(record.getStudyId());
            before = record.getDate();
            if(record.getStudyStatus() == StudyStatus.REST) {
                restTime += record.getEndTime()-record.getStartTime();
            }
            else if(record.getStudyStatus() == StudyStatus.STUDY) {
                beforeStartTime = Math.min(beforeStartTime, record.getStartTime());
                beforeEndTime = Math.max(beforeEndTime, record.getEndTime());
            }

        }
        if(start == date){
            startTimeAvg += beforeStartTime;
            endTimeAvg += beforeEndTime;
            studyTime += beforeEndTime-beforeStartTime;
            days++;
        }

        if(days != 0) {
            startTimeAvg = startTimeAvg / days;
            endTimeAvg = endTimeAvg / days;
        }

        List<Long> studyIds = new ArrayList<>(studyIdSet);

        log.info("Before call study-service");
        Result<List<StudyDto>> result = circuitBreaker.run(() -> studyServiceClient.getStudies(studyIds), throwable -> new Result<>());
        log.info("After call study-service");

        List<StudyDto> studies = result.getData();

        return StudyRecord.createMemberRecords(studies, memberId, studyTime, startTimeAvg, endTimeAvg, restTime, records);
    }

    public StudyRecord getRecords(String period, LocalDate date, StudyCategory studyCategory) {
        LocalDate start = getStartDate(period, date);
        List<RecordAvgDto> records = recordRepository.findAvgRecordByStudyCategory(studyCategory, start, date);

        long startTimeAvg = 0;
        long endTimeAvg = 0;

        List<RecordDto> list = new ArrayList<>();

        LocalDate i = start;
        int count = 0;
        long startTime = 0;
        long endTime = 0;
        for (RecordAvgDto record: records) {
            if (!record.getDate().isEqual(i)) {
                if (count != 0) {
                    startTime = startTimeAvg / count;
                    endTime = endTimeAvg / count;
                }
                list.add(RecordDto.createStudyRecord(null, startTime, endTime, i));

                i = i.plusDays(1);
                startTimeAvg = 0;
                endTimeAvg = 0;
                count = 0;

            }
            count++;
            startTimeAvg += record.getStartTime();
            endTimeAvg += record.getEndTime();
        }
        if(count != 0) {
            startTime = startTimeAvg / count;
            endTime = endTimeAvg / count;
            list.add(RecordDto.createStudyRecord(null, startTime, endTime, i));
        }

        return new StudyRecord(null, null, 0L, 0L, 0L, 0L, list);

    }

    public Result<List<RecordRankedResponse>> getRankedRecord(String period, LocalDate date, StudyStatus studyStatus,
                                                              StudyCategory studyCategory, String memberId, Long studyId,
                                                              int page, int size) {
        LocalDate start = getStartDate(period, date);
        PageRequest request = PageRequest.of(page, size);
        Page<RecordRankedDto> timeRecords
                = recordRepository.findTimeRecord(start, date, studyStatus, studyCategory, memberId, studyId, request);

        List<RecordRankedDto> records = timeRecords.getContent();

        List<String> memberIds = records.stream().map(RecordRankedDto::getMemberId).collect(Collectors.toList());

        log.info("Before call member-service");
        List<MemberDto> memberDtos = circuitBreaker.run(() -> memberServiceClient.getMembers(memberIds),
                throwable -> new ArrayList<>());
        log.info("After call member-service");

        Map<String, String> idToName
                = memberDtos.stream().collect(Collectors.toMap(MemberDto::getMemberId, MemberDto::getName));

        List<RecordRankedResponse> responses = records.stream().map(record -> {
            RecordRankedResponse response = RecordRankedResponse.from(record);
            response.setMemberName(idToName.get(record.getMemberId()));
            return response;
        }).sorted((a, b) -> (int) (b.getTime() - a.getTime())).collect(Collectors.toList());

        for(int i=page*10; i<responses.size(); i++) {
            responses.get(i-page*10).setRank(i+1);
        }

        return new Result<>(responses, timeRecords.getTotalPages(), timeRecords.getNumber(), timeRecords.hasNext());
    }

    public Result<List<RecordRankedResponse>> getRankedRecordByDate(String period, LocalDate date, StudyStatus studyStatus,
                                                              StudyCategory studyCategory, String memberId, Long studyId,
                                                              int page, int size) {
        LocalDate start = getStartDate(period, date);
        PageRequest request = PageRequest.of(page, size);
        List<RecordRankedResponse> list = new ArrayList<>();

        int max = 0;
        switch (period) {
            case "WEEK":
                max = 7;
                break;
            case "MONTH":
                max = LocalDate.EPOCH.lengthOfMonth()-1;
                break;
        }

        for(int i=0; i<max; i++) {
            Page<RecordRankedDto> result
                    = recordRepository.findTimeRecordByDate(start.plusDays(i), start.plusDays(i),
                    StudyStatus.STUDY, studyCategory, memberId, studyId, request);

            List<RecordRankedDto> content = result.getContent();
            long studyTime = 0;
            for (RecordRankedDto record: content) {
                studyTime += record.getStudyTime();
            }
            RecordRankedResponse response = new RecordRankedResponse();
            response.setDate(start.plusDays(i));
            response.setTime(studyTime>0 ? studyTime/content.size() : 0);
            list.add(response);
        }

        return new Result<>(list, 0, 0, false);
    }

    private LocalDate getStartDate(String period, LocalDate date) {
        LocalDate start;
        switch (period) {
            case "DAY":
                start = date;
                break;
            case "WEEK":
                start = date.minusWeeks(1).plusDays(1);
                break;
            case "MONTH":
                Month month = date.getMonth();
                start = LocalDate.of(date.getYear(), month, 1);
                break;
            default:
                throw new RuntimeException("잘못된 접근입니다");
        }
        return start;
    }

    private int getEndDayOfMonth(LocalDate date) {
        int monthValue = date.getMonthValue();
        int[] days = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if(date.getYear()%4 == 0) {
            days[1] = 29;
        }
        return days[monthValue];
    }

    public StudyRecord getRecords(String period, LocalDate date, Long studyId) {
        LocalDate start;
        LocalDate end = date;
        start = getStartDate(period, date);

        List<RecordDto> memberRecords = recordRepository.findAllRecord(studyId, start, end);
        List<RecordDto> result = memberRecords.stream()
                .filter(record -> record.getStudyStatus().equals(StudyStatus.STUDY))
                .collect(Collectors.toList());

        Set<String> counter = new HashSet<>();
        long memberStudyTime = 0;
        if(result.size() != 0) {
            List<RecordDto> studyResult = new ArrayList<>();
            long studyTime = 0;
            long startTime = result.get(0).getStartTime();
            LocalDate beforeDate = result.get(0).getDate();

            for(int i=0; i<result.size(); i++) {
                if (result.get(i).getDate().equals(beforeDate)) {
                    studyTime += result.get(i).getEndTime() - result.get(i).getStartTime();
                    counter.add(result.get(i).getMemberId());
                } else {
                    RecordDto studyRecord
                            = RecordDto.createStudyRecordWithCount(studyId, startTime, (startTime + studyTime), beforeDate, counter.size());
//                    RecordDto studyRecord = RecordDto.createStudyRecord(studyId, startTime, (startTime + studyTime), beforeDate);
                    studyResult.add(studyRecord);
                    beforeDate = beforeDate.plusDays(1);
                    studyTime = result.get(i).getEndTime() - result.get(i).getStartTime();
                    startTime = result.get(i).getStartTime();
                    counter.clear();
                }

                if(result.get(i).getDate().equals(beforeDate) && result.size()-1 == i) {
                    RecordDto studyRecord
                            = RecordDto.createStudyRecordWithCount(studyId, startTime, (startTime + studyTime), beforeDate, counter.size());
//                    RecordDto studyRecord = RecordDto.createStudyRecord(studyId, startTime, (startTime + studyTime), beforeDate);
                    studyResult.add(studyRecord);
                    beforeDate = beforeDate.plusDays(1);
                    studyTime = result.get(i).getEndTime() - result.get(i).getStartTime();
                    startTime = result.get(i).getStartTime();
                    counter.clear();
                }

                memberStudyTime += result.get(i).getEndTime() - result.get(i).getStartTime();
            }

            result = studyResult;
        }

        log.info("Before call study-service");
        Result<List<StudyDto>> studyResult = circuitBreaker.run(() -> studyServiceClient.getStudies(List.of(studyId)),
                throwable -> new Result<>());
        log.info("After call study-service");

        List<StudyDto> studies = studyResult.getData();

        return StudyRecord.createMemberStudyRecords(studies, null, memberStudyTime, result);
    }

    public StudyRecord getRecords(Long groupId) {

        log.info("Before call group-service");
        GroupDto group = circuitBreaker.run(() -> groupClientService.getGroup(groupId), throwable -> new GroupDto());
        log.info("After call group-service");

        List<String> memberIds = group.getMembers().stream().map(GroupMemberDto::getMemberId).collect(Collectors.toList());
        Long studyId = group.getStudy().getStudyId();
        LocalDate date = LocalDate.now();
        LocalDate start;
        LocalDate end = date.plusDays(1);
        start = getStartDate("MONTH", date);

        List<RecordDto> memberRecords = recordRepository.findAllMembersRecord(memberIds, studyId, start, end);
        List<RecordDto> result = memberRecords.stream()
                .filter(record -> record.getStudyStatus().equals(StudyStatus.STUDY))
                .collect(Collectors.toList());

        Set<String> counter = new HashSet<>();
        long memberStudyTime = 0;
        if(result.size() != 0) {
            List<RecordDto> studyResult = new ArrayList<>();
            long studyTime = 0;
            long startTime = result.get(0).getStartTime();
            LocalDate beforeDate = result.get(0).getDate();

            for(int i=0; i<result.size(); i++) {
                if (result.get(i).getDate().equals(beforeDate)) {
                    studyTime += result.get(i).getEndTime() - result.get(i).getStartTime();
                    counter.add(result.get(i).getMemberId());
                } else {
                    RecordDto studyRecord = RecordDto.createStudyRecord(studyId, startTime, (startTime + studyTime), beforeDate);
                    studyResult.add(studyRecord);
                    beforeDate = beforeDate.plusDays(1);
                    studyTime = result.get(i).getEndTime() - result.get(i).getStartTime();
                    startTime = result.get(i).getStartTime();
                    counter.clear();
                }

                if(result.get(i).getDate().equals(beforeDate) && result.size()-1 == i) {
                    RecordDto studyRecord = RecordDto.createStudyRecord(studyId, startTime, (startTime + studyTime), beforeDate);
                    studyResult.add(studyRecord);
                    beforeDate = beforeDate.plusDays(1);
                    studyTime = result.get(i).getEndTime() - result.get(i).getStartTime();
                    startTime = result.get(i).getStartTime();
                    counter.clear();
                }

                memberStudyTime += result.get(i).getEndTime() - result.get(i).getStartTime();
            }

            result = studyResult;
        }

        log.info("Before call study-service");
        Result<List<StudyDto>> studyResult = circuitBreaker.run(() -> studyServiceClient.getStudies(List.of(studyId)),
                throwable -> new Result<>());
        log.info("After call study-service");

        List<StudyDto> studies = studyResult.getData();

        return StudyRecord.createMemberStudyRecords(studies, null, memberStudyTime, result);
    }

    public StudyRecord getRawRecords(String memberId) {
        LocalDate date = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        List<Record> records;
        if(now.getHour()<5) {
            records = recordRepository.findByDateAndTime(date.minusDays(1), memberId, 3600*5, 3600*29-60);
        }
        else {
            records = recordRepository.findByDateAndTime(date, memberId, 3600*5, 3600*24);
        }

        List<Long> studyIds = new ArrayList<>();
        records.forEach(record -> studyIds.add(record.getStudyId()));

        log.info("Before call study-service");
        Result<List<StudyDto>> studies = circuitBreaker.run(() -> studyServiceClient.getStudies(studyIds), throwable -> new Result<>());
        log.info("After call study-service");

        List<RecordDto> result = records.stream().map(RecordDto::createRawRecord).collect(Collectors.toList());


        return new StudyRecord(studies.getData(), memberId, 0, 0, 0, 0, result);


    }

    public RecordCount getCount(String period, LocalDate date, Long studyId) {
        LocalDate start;
        start = getStartDate(period, date);

        RecordCount recordCount = recordRepository.findCount(start, date, studyId);

        return recordCount;
    }
}

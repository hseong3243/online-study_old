package study.recordservice.dto;

import lombok.Data;
import study.recordservice.domain.Record;
import study.recordservice.domain.StudyCategory;
import study.recordservice.domain.StudyStatus;

import java.time.LocalDate;

@Data
public class RecordDto {

    private Long studyId;
    private long startTime;
    private long endTime;
    private LocalDate date;
    private StudyStatus studyStatus;
    private String memberId;
    private int count;

    public static RecordDto createStudyRecord(Long studyId, long startTime, long endTime, LocalDate date) {
        RecordDto recordDto = new RecordDto();
        recordDto.studyId = studyId;
        recordDto.startTime = startTime;
        recordDto.endTime = endTime;
        recordDto.date = date;
        recordDto.studyStatus = StudyStatus.STUDY;

        return recordDto;
    }

    public static RecordDto createStudyRecordWithCount(Long studyId, long startTime, long endTime, LocalDate date, int count) {
        RecordDto recordDto = new RecordDto();
        recordDto.studyId = studyId;
        recordDto.startTime = startTime;
        recordDto.endTime = endTime;
        recordDto.date = date;
        recordDto.studyStatus = StudyStatus.STUDY;
        recordDto.count = count;

        return recordDto;
    }

    public static RecordDto createRawRecord(Record record) {
        RecordDto recordDto = new RecordDto();
        recordDto.studyId = record.getStudyId();
        recordDto.startTime = record.getStartTime();
        recordDto.endTime = record.getEndTime();
        recordDto.date = record.getDate();
        recordDto.studyStatus =record.getStudyStatus();
        recordDto.memberId = record.getMemberId();
        return recordDto;
    }
}

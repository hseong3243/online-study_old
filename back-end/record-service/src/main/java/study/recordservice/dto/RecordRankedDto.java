package study.recordservice.dto;

import lombok.Data;
import study.recordservice.domain.StudyCategory;
import study.recordservice.domain.StudyStatus;

import java.time.LocalDate;

@Data
public class RecordRankedDto {
    private String memberId;
    private StudyCategory studyCategory;
    private StudyStatus studyStatus;
    private long startTime;
    private long endTime;
    private int rank;
    private long studyTime;
    private LocalDate date;

    public static RecordRankedDto create(String memberId, StudyCategory studyCategory, StudyStatus studyStatus, long startTime, long endTime) {
        RecordRankedDto recordRankedDto = new RecordRankedDto();
        recordRankedDto.memberId=memberId;
        recordRankedDto.studyCategory=studyCategory;
        recordRankedDto.studyStatus = studyStatus;
        recordRankedDto.startTime=startTime;
        recordRankedDto.endTime=endTime;
        return recordRankedDto;
    }
}

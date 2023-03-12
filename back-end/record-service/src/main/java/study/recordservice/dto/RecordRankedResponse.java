package study.recordservice.dto;

import lombok.Data;
import study.recordservice.domain.StudyCategory;
import study.recordservice.domain.StudyStatus;

import java.time.LocalDate;

@Data
public class RecordRankedResponse {
    private int rank;
    private String memberName;
    private StudyCategory studyCategory;
    private StudyStatus studyStatus;
    private long time;
    private LocalDate date;

    public static RecordRankedResponse from(RecordRankedDto recordRankedDto) {
        RecordRankedResponse recordRankedResponse = new RecordRankedResponse();
        recordRankedResponse.studyCategory = recordRankedDto.getStudyCategory();
        recordRankedResponse.studyStatus = recordRankedDto.getStudyStatus();
        recordRankedResponse.time = recordRankedDto.getEndTime() - recordRankedDto.getStartTime();
        return recordRankedResponse;
    }
}

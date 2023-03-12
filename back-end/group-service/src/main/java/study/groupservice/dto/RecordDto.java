package study.groupservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RecordDto {
    private Long studyId;
    private long startTime;
    private long endTime;
    private LocalDate date;
    private StudyStatus studyStatus;
}

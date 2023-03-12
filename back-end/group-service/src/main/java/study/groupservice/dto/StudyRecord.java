package study.groupservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class StudyRecord {

    private List<Long> studyId;
    private String memberId;
    private long studyTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long startTimeAvg;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long endTimeAvg;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long restTime;

    private List<RecordDto> records;
}

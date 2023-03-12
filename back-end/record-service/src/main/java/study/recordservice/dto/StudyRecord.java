package study.recordservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudyRecord {

    private List<StudyDto> studies = new ArrayList<>();
    private String memberId;
    private long studyTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long startTimeAvg;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long endTimeAvg;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long restTime;

    private List<RecordDto> records;

    public static StudyRecord createMemberStudyRecords(List<StudyDto> studies, String memberId, long studyTime, List<RecordDto> records) {
        StudyRecord studyRecord = new StudyRecord();
        studyRecord.studies = studies;
        studyRecord.memberId = memberId;
        studyRecord.studyTime = studyTime;
        studyRecord.records = records;
        return studyRecord;
    }

    public static StudyRecord createMemberRecords(List<StudyDto> studies, String memberId, long studyTime,
                                                 long startTimeAvg, long endTimeAvg, long restTime, List<RecordDto> records) {
        StudyRecord studyRecord = new StudyRecord();
        studyRecord.studies = studies;
        studyRecord.memberId = memberId;
        studyRecord.studyTime = studyTime;
        studyRecord.startTimeAvg = startTimeAvg;
        studyRecord.endTimeAvg = endTimeAvg;
        studyRecord.restTime = restTime;
        studyRecord.records = records;
        return studyRecord;
    }
}

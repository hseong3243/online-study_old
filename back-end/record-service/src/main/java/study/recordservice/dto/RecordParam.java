package study.recordservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordParam {
    private String memberId;
    private int testCode;
    private LocalDate date;
    private Long studyId;
}

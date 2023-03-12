package study.recordservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordAvgDto {

    private LocalDate date;
    private long startTime;
    private long endTime;
}

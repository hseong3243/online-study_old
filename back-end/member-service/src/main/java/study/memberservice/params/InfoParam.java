package study.memberservice.params;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import java.time.LocalDate;

@Data
public class InfoParam {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate dDay;
    private String goal;
    @Max(value = 30)
    private String promise;
    private String memberId;
    private Long targetTime;
}

package study.recordservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MemberInfoDto {
    private Long memberInfoId;
    private LocalDate dDay;
    private String goal;
    private String promise;
    private Long targetTime;
}

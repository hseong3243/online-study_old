package study.memberservice.dto;

import lombok.Data;
import study.memberservice.domain.MemberInfo;

import java.time.LocalDate;

@Data
public class MemberInfoDto {
    private Long memberInfoId;
    private LocalDate dDay;
    private String goal;
    private String promise;
    private Long targetTime;

    public static MemberInfoDto from(MemberInfo memberInfo) {
        MemberInfoDto memberInfoDto = new MemberInfoDto();
        memberInfoDto.memberInfoId = memberInfo.getId();
        memberInfoDto.dDay = memberInfo.getDDay();
        memberInfoDto.goal = memberInfo.getGoal();
        memberInfoDto.promise = memberInfo.getPromise();
        memberInfoDto.targetTime = memberInfo.getTargetTime();
        return memberInfoDto;
    }
}

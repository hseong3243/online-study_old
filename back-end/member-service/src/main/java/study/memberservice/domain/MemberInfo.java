package study.memberservice.domain;

import lombok.Getter;
import study.memberservice.params.InfoParam;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Entity
@Getter
public class MemberInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dDay;
    private String goal;
    private String promise;
    private Long targetTime = 0L;

    public static MemberInfo create(InfoParam infoParam) {
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.dDay = infoParam.getDDay();
        memberInfo.goal = infoParam.getGoal();
        memberInfo.promise = infoParam.getPromise();
        memberInfo.targetTime = infoParam.getTargetTime();

        return memberInfo;
    }

    public void changePromise(String promise) {
        this.promise = promise;
    }

    public void changeDDay(LocalDate dDay) {
        this.dDay = dDay;
    }

    public void changeGoal(String goal) {
        this.goal = goal;
    }

    public void changeTargetTime(long targetTime) {
        this.targetTime = targetTime;
    }
}

package study.memberservice.dto;

import lombok.Data;
import study.memberservice.domain.Member;
import study.memberservice.domain.StudyCategory;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class MemberDto {

    private Long id;
    @NotNull(message = "Email cannot be null")
    @Email
    private String email;
    @NotNull(message = "Password cannot be null")
    private String password;
    @NotNull(message = "name cannot be null")
    @Max(value = 7)
    private String name;
    private StudyCategory studyCategory;
    private String memberId;

    private MemberInfoDto memberInfo;

    private List<GroupDto> groups = new ArrayList<>();

    public void setGroups(List<GroupDto> groups) {
        this.groups = groups;
    }

    public static MemberDto from(Member member) {
        MemberDto memberDto = new MemberDto();
        memberDto.id = member.getId();
        memberDto.email = member.getEmail();
        memberDto.password = member.getPassword();
        memberDto.name = member.getName();
        memberDto.studyCategory = member.getStudyCategory();
        memberDto.memberId = member.getMemberId();
        memberDto.memberInfo = MemberInfoDto.from(member.getMemberInfo());
        return memberDto;
    }
}

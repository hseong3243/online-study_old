package study.recordservice.dto;

import lombok.Data;
import study.recordservice.domain.StudyCategory;

@Data
public class MemberDto {
    private Long id;
    private String email;
    private String password;
    private String name;
    private StudyCategory studyCategory;
    private String memberId;
    private MemberInfoDto memberInfo;
}

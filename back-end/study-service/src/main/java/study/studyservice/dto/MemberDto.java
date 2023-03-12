package study.studyservice.dto;

import lombok.Data;
import study.studyservice.domain.StudyCategory;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class MemberDto {

    private Long id;
    private String email;
    private String password;
    private String name;
    private StudyCategory studyCategory;
    private String memberId;
}

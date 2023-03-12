package study.memberservice.params;

import lombok.Data;
import study.memberservice.domain.StudyCategory;

@Data
public class MemberParams {

    private String name;
    private StudyCategory studyCategory;
    private String password;
    private String updatePassword;
    private String updatePasswordCheck;
}

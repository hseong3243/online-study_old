package study.groupservice.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MemberDto {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String memberId;
}

package study.memberservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GroupMemberDto {
    private Long groupMemberId;
    private String memberId;
    private String memberName;
    private LocalDate joinedAt;
    private long attend;
    private GroupRole groupRole;
}

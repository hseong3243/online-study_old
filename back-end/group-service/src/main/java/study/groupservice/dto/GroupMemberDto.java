package study.groupservice.dto;

import lombok.Data;
import study.groupservice.domain.GroupMember;
import study.groupservice.domain.GroupRole;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
public class GroupMemberDto {

    private Long groupMemberId;
    private String memberId;
    private String memberName;
    private LocalDate joinedAt;
    private long attend;
    private GroupRole groupRole;

    public static GroupMemberDto from(GroupMember groupMember) {
        GroupMemberDto groupMemberDto = new GroupMemberDto();
        groupMemberDto.groupMemberId = groupMember.getId();
        groupMemberDto.memberId = groupMember.getMemberId();
        groupMemberDto.groupRole = groupMember.getRole();
        groupMemberDto.attend = groupMember.getAttend().toEpochSecond(ZoneOffset.of("+09:00"));
        groupMemberDto.joinedAt = groupMember.getJoinedAt();
        return groupMemberDto;
    }
}

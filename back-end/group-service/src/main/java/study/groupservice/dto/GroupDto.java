package study.groupservice.dto;

import lombok.Data;
import study.groupservice.domain.Group;
import study.groupservice.domain.GroupCategory;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Data
public class GroupDto {

    private Long groupId;
    private String name;
    private String intro;
    private long createdAt;
    private int level;
    private GroupCategory groupCategory;
    private String memberId;
    private String memberName;
//    private Long studyId;
    private StudyDto study;
    private List<PurposeDto> purposes = new ArrayList<>();
    private List<GroupMemberDto> members = new ArrayList<>();
    private List<RoomDto> rooms = new ArrayList<>();

    public void setRooms(List<RoomDto> rooms) {
        this.rooms = rooms;
    }

    public void setStudy(StudyDto study) {
        this.study = study;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public static GroupDto from(Group group) {
        GroupDto groupDto = new GroupDto();
        groupDto.groupId = group.getId();
        groupDto.name = group.getName();
        groupDto.intro = group.getIntro();
        groupDto.createdAt = group.getCreatedAt().atStartOfDay().toEpochSecond(ZoneOffset.of("+09:00"));
        groupDto.groupCategory = group.getGroupCategory();
        groupDto.level = group.getLevel();
        groupDto.memberId = group.getMemberId();
        group.getGroupPurposes().forEach(groupPurpose -> {
            PurposeDto purposeDto = PurposeDto.from(groupPurpose.getPurpose());
            groupDto.purposes.add(purposeDto);
        });
        return groupDto;
    }
}

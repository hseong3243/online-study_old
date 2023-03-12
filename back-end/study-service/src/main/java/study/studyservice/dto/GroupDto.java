package study.studyservice.dto;

import lombok.Data;

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
}

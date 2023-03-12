package study.groupservice.domain;

import lombok.Getter;
import study.groupservice.params.GroupParams;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Entity(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    private String name;
    private String intro;
    private LocalDate createdAt;
    private int level;

    @Enumerated(EnumType.STRING)
    private GroupCategory groupCategory;

    private String memberId;
    private Long studyId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group", cascade = CascadeType.ALL)
    private List<GroupMember> groupMembers;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group", cascade = CascadeType.ALL)
    private List<GroupPurpose> GroupPurposes;

    public void changeIntro(String intro){
        this.intro = intro;
    }

    public static Group create(GroupParams groupParams, String memberId) {
        Group group = new Group();
        group.name = groupParams.getName();
        group.memberId = memberId;
        group.studyId = groupParams.getStudyId();
        group.createdAt = LocalDate.now();
        group.groupCategory = groupParams.getGroupCategory();
        group.level = 1;
        return group;
    }
}

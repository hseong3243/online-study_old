package study.groupservice.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberId;
    private LocalDate joinedAt;
    private LocalDateTime attend;

    @Enumerated(EnumType.STRING)
    private GroupRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    public void upgradeRole() {
        this.role = GroupRole.BOSS;
    }

    public void attendance() {
        this.attend = LocalDateTime.now();
    }

    public void setAttend(LocalDateTime attend) {
        this.attend = attend;
    }

    public static GroupMember create(Group group, String memberId) {
        GroupMember groupMember = new GroupMember();
        groupMember.memberId = memberId;
        groupMember.role = GroupRole.USER;
        groupMember.group = group;
        groupMember.attend = LocalDateTime.now().minusDays(1);
        groupMember.joinedAt = LocalDate.now();
        return groupMember;
    }
}

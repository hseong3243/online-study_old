package study.groupservice.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class GroupPurpose {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purpose_id")
    private Purpose purpose;

    public void setGroupAndPurpose(Group group, Purpose purpose) {
        this.group = group;
        this.purpose = purpose;
        group.getGroupPurposes().add(this);
    }
}

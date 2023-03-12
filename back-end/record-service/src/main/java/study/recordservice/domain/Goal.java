package study.recordservice.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Long id;

    private String memberId;
    private Long targetTime;
}

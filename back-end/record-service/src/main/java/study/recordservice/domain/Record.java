package study.recordservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_record_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private StudyStatus studyStatus;
    @Enumerated(EnumType.STRING)
    private StudyCategory studyCategory;

    private long startTime;
    private long endTime;
    private LocalDate date;


    private String memberId;
    private Long studyId;

    public Record(StudyStatus studyStatus, StudyCategory studyCategory,
                  long startTime, long endTime, LocalDate date, String memberId, Long studyId) {
        this.studyStatus = studyStatus;
        this.studyCategory = studyCategory;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.memberId = memberId;
        this.studyId = studyId;
    }

}

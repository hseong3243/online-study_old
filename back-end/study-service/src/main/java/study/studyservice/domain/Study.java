package study.studyservice.domain;

import lombok.Getter;
import study.studyservice.dto.StudyDto;

import javax.persistence.*;

@Getter
@Entity
public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private Long id;

    private String name;

    public static Study from(StudyDto studyDto) {
        Study study = new Study();
        study.name = studyDto.getName();
        return study;
    }
}

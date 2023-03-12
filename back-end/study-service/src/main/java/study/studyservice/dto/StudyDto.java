package study.studyservice.dto;

import lombok.Data;
import study.studyservice.domain.Study;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class StudyDto {

    private Long studyId;

    @NotNull(message = "study name cannot be null")
    @Size(min = 2)
    private String name;

    public static StudyDto from(Study study) {
        StudyDto studyDto = new StudyDto();
        studyDto.studyId = study.getId();
        studyDto.name = study.getName();
        return studyDto;
    }
}

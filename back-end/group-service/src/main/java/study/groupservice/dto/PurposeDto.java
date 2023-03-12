package study.groupservice.dto;

import lombok.Data;
import study.groupservice.domain.Purpose;
import study.groupservice.domain.PurposeCategory;

@Data
public class PurposeDto {

    private Long purposeId;
    private String content;
    private PurposeCategory purposeCategory;

    public static PurposeDto from(Purpose purpose) {
        PurposeDto purposeDto = new PurposeDto();
        purposeDto.purposeId = purpose.getId();
        purposeDto.content = purpose.getContent();
        purposeDto.purposeCategory = purpose.getPurposeCategory();
        return purposeDto;
    }
}

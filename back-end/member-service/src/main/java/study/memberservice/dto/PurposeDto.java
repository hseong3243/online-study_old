package study.memberservice.dto;

import lombok.Data;

@Data
public class PurposeDto {
    private Long purposeId;
    private String content;
    private PurposeCategory purposeCategory;
}

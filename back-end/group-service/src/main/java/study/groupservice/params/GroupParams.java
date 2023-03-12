package study.groupservice.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import study.groupservice.domain.GroupCategory;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupParams {

    @NonNull
    private String name;
    private Long studyId;
    private String intro;
    private GroupCategory groupCategory;
}

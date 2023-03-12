package study.groupservice.domain;

import lombok.Getter;
import study.groupservice.params.PurposeParam;

import javax.persistence.*;

@Entity
@Getter
public class Purpose {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @Enumerated(EnumType.STRING)
    private PurposeCategory purposeCategory;

    public void setPurposeCategory(PurposeCategory category){
        this.purposeCategory = category;
    }

    public static Purpose create(PurposeParam purposeParam) {
        Purpose purpose = new Purpose();
        purpose.content = purposeParam.getContent();
        return purpose;
    }
}

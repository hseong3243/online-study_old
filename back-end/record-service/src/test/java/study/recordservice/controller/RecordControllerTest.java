package study.recordservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.recordservice.service.RecordService;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
public class RecordControllerTest {

    private final RecordService recordService;
    private final EntityManager em;

    public RecordControllerTest(RecordService recordService, EntityManager em) {
        this.recordService = recordService;
        this.em = em;
    }


    @Test
    public void searchData() {
        //given

        //when

        //then

    }
}

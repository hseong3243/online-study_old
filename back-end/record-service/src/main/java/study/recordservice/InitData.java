package study.recordservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import study.recordservice.dto.MemberDto;
import study.recordservice.client.MemberServiceClient;
import study.recordservice.domain.Record;
import study.recordservice.domain.StudyCategory;
import study.recordservice.domain.StudyStatus;
import study.recordservice.repository.RecordRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class InitData {

    private final RecordRepository recordRepository;
    private final MemberServiceClient memberServiceClient;


    String memberHigh = "87a59419-8912-4272-a4c7-7be1a0bcb958";
    String memberGov = "e84def0d-45cc-4add-be8e-1ca32cef091b";
    String memberLang = "dcc2d56f-505a-4f0c-86ec-08099b46394f";
    String memberJob = "12f9c218-413c-48cd-8474-ca50a4759265";
    String memberEtc = "48d335a4-78b1-4065-adf2-84d68c3890ce";

//    @PostConstruct
    public void recordInit() {
        List<MemberDto> members = memberServiceClient.testMembers();
        Random random = new Random();
        LocalDate now = LocalDate.now();
        now = now.minusMonths(1);

        List<Record> records = new ArrayList<>();
        long startTime;
        long endTime;
        LocalDate date;
        String memberId;
        Long studyId;
        for(MemberDto member: members) {
            date = now;
            for (int i = 0; i < 20; i++) {
                date = date.plusDays(random.nextInt(3)+1);
                startTime = 3600 * 16 - random.nextInt(3600);
                endTime = 3600 * 19 - random.nextInt(3600);
                studyId = 256L;

                records.add(new Record(StudyStatus.STUDY, member.getStudyCategory(), startTime, endTime, date, member.getMemberId(), studyId));

                startTime = endTime;
                endTime = 3600 * 20 - random.nextInt(3600);
                records.add(new Record(StudyStatus.REST, member.getStudyCategory(), startTime, endTime, date, member.getMemberId(), studyId));

                startTime = endTime;
                endTime = 3600 * 23 - random.nextInt(3600);
                records.add(new Record(StudyStatus.STUDY, member.getStudyCategory(), startTime, endTime, date, member.getMemberId(), studyId));
//            ends.add(endTime);
            }
        }

        recordRepository.saveAll(records);
    }

//    @PostConstruct
    public void init2() {


        Random random = new Random();
        LocalDate now = LocalDate.now();
        now = now.minusMonths(1);
        for(int i=0; i<60; i++) {
            Record recordHigh = new Record(StudyStatus.STUDY, StudyCategory.HIGH, 3600 * 9 + 1800 - random.nextInt(3600),
                    3600 * 22 + 1800 - random.nextInt(3600), now.plusDays(i), memberHigh, 526L);
            recordRepository.save(recordHigh);

            Record recordGov = new Record(StudyStatus.STUDY, StudyCategory.GOV, 3600 * 8 - random.nextInt(3600), 3600 * 22 - random.nextInt(3600),
                    now.plusDays(i), memberGov, 532L);
            recordRepository.save(recordGov);

            Record recordLang = new Record(StudyStatus.STUDY, StudyCategory.LANG, 3600 * 18 - random.nextInt(3600), 3600 * 21 - random.nextInt(3600),
                    now.plusDays(i), memberLang, 533L);
            recordRepository.save(recordLang);

            Record recordJob = new Record(StudyStatus.STUDY, StudyCategory.JOB, 3600 * 10 - random.nextInt(3600), 3600 * 17 - random.nextInt(3600),
                    now.plusDays(i), memberJob, 534L);
            recordRepository.save(recordJob);

            Record recordEtc = new Record(StudyStatus.STUDY, StudyCategory.ETC, 3600 * 19 - random.nextInt(3600), 3600 * 21 - random.nextInt(3600),
                    now.plusDays(i), memberEtc, 528L);
            recordRepository.save(recordEtc);
        }
    }

//    @PostConstruct
    public void init() {
        long startTime;
        long endTime;
        LocalDate date;
        String memberId;
        Long studyId;
        StudyStatus studyStatus;

        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        LocalDate startDate = LocalDate.now().minusMonths(1);
        date = startDate;

        List<Record> list = new ArrayList<>();
        List<Long> ends = new ArrayList<>();
        String[] uuid = {"2e0147db-917f-466a-bbfb-709331d88dcf", "f9f6d61e-b631-4728-a1e5-63e7c2019e10"};

        for(int j=0; j<2; j++) {
            memberId = uuid[j];
            date = startDate;
            for (int i = 0; i < 60; i++) {
                date = date.plusDays(1);
                startTime = 3600 * 9 - random.nextInt(3600);
                endTime = 3600 * 12 - random.nextInt(3600);
                studyId = 256L;

                list.add(new Record(StudyStatus.STUDY, StudyCategory.UNIV, startTime, endTime, date, memberId, studyId));

                startTime = endTime;
                endTime = 3600 * 13 - random.nextInt(1800);
                list.add(new Record(StudyStatus.REST, StudyCategory.UNIV, startTime, endTime, date, memberId, studyId));

                startTime = endTime;
                endTime = 3600 * 15 - random.nextInt(3600);
                list.add(new Record(StudyStatus.STUDY, StudyCategory.UNIV, startTime, endTime, date, memberId, studyId));
                ends.add(endTime);
            }
        }

        for(int j=0; j<2; j++) {
            memberId = uuid[j];
            date = startDate;
            for (int i = 0; i < 60; i++) {
                date = date.plusDays(1);
                startTime = ends.get(i);
                endTime = 3600 * 18 - random.nextInt(3600);
                studyId = 255L;

                list.add(new Record(StudyStatus.STUDY, StudyCategory.UNIV, startTime, endTime, date, memberId, studyId));

                startTime = endTime;
                endTime = 3600 * 19 - random.nextInt(3600);
                list.add(new Record(StudyStatus.REST, StudyCategory.UNIV, startTime, endTime, date, memberId, studyId));

                startTime = endTime;
                endTime = 3600 * 22 - random.nextInt(3600);
                list.add(new Record(StudyStatus.STUDY, StudyCategory.UNIV, startTime, endTime, date, memberId, studyId));
            }
        }

        recordRepository.saveAll(list);

    }

}

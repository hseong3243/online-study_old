package study.recordservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.recordservice.dto.MemberDto;
import study.recordservice.client.MemberServiceClient;
import study.recordservice.repository.RecordRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final MemberServiceClient memberServiceClient;
    private final RecordRepository recordRepository;

    @GetMapping("/delete")
    @Transactional
    public ResponseEntity<String> deleteTestData() {
        List<MemberDto> memberDtos = memberServiceClient.testMembers();
        List<String> memberIds = memberDtos.stream().map(MemberDto::getMemberId).collect(Collectors.toList());

        recordRepository.deleteTestData(memberIds);

        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }
}

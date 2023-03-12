package study.memberservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.memberservice.params.InfoParam;
import study.memberservice.service.MemberInfoService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Slf4j
public class MemberInfoController {

    private final MemberInfoService memberInfoService;

    @PostMapping("/member-info")
    public ResponseEntity<Long> createInfo(@RequestBody InfoParam infoParam) {
        Long memberInfoId = memberInfoService.createInfo(infoParam);

        return ResponseEntity.status(HttpStatus.OK).body(memberInfoId);
    }

    @PostMapping("/member-info/{memberId}")
    public ResponseEntity<Long> updateInfo(@PathVariable String memberId,
                                           @RequestBody InfoParam infoParam) {

        log.info("들어온 데이터: {}", infoParam);
        Long memberInfoId = memberInfoService.updateInfo(memberId, infoParam);

        return ResponseEntity.status(HttpStatus.OK).body(memberInfoId);
    }
}

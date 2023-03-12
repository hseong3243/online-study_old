package study.groupservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.groupservice.domain.Group;
import study.groupservice.domain.GroupCategory;
import study.groupservice.domain.PostCategory;
import study.groupservice.dto.*;
import study.groupservice.params.CommentParam;
import study.groupservice.params.GroupParams;
import study.groupservice.params.PostParam;
import study.groupservice.params.PurposeParam;
import study.groupservice.service.GroupService;
import study.groupservice.service.PostService;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Slf4j
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/groups")
    public ResponseEntity<Result<List<GroupDto>>> getGroups(@RequestParam(required = false) String memberId,
                                                            @RequestParam(required = false) GroupCategory groupCategory,
                                                            @RequestParam(name = "order", required = false) String order,
                                                            @RequestParam(name = "search", required = false) String search,
                                                            @RequestParam(name = "page") int page,
                                                            @RequestParam(name = "size") int size) {
        Page<GroupDto> groups;
        if(groupCategory != null){
            groups = groupService.getGroupsByGroupCategory(groupCategory, page, size, order, search);
        }
        else if(memberId != null) {
            groups = groupService.getGroups(memberId, page, size, order, search);
        }
        else {
            groups = groupService.getGroups(page, size, order, search);
        }

        log.info("테스트");

        return ResponseEntity.status(OK).body(new Result<>(groups.getContent(),
                groups.getTotalPages(), groups.getNumber(), groups.hasNext(), groups.hasPrevious()));
    }

    @PostMapping("/groups")
    public ResponseEntity<Long> createGroup(@RequestBody GroupParams groupParams,
                                            Principal principal) {
        String memberId = principal.getName();
        Long groupId = groupService.createGroup(groupParams, memberId);

        return ResponseEntity.status(CREATED).body(groupId);
    }

    @GetMapping("/groups/{groupId}")
    public ResponseEntity<GroupDto> getGroup(@PathVariable Long groupId) {
        GroupDto groupDto = groupService.getGroup(groupId);

        return ResponseEntity.status(OK).body(groupDto);
    }

    @PostMapping("/groups/{groupId}")
    public ResponseEntity<Long> updateGroup(@PathVariable Long groupId,
                                            @RequestBody GroupParams groupParams,
                                            Principal principal) {
        String memberId = principal.getName();
        Long updatedGroupId = groupService.updateIntro(groupId, groupParams, memberId);

        return ResponseEntity.status(CREATED).body(updatedGroupId);
    }

    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity<String> deleteGroup(@PathVariable Long groupId,
                                            Principal principal) {
        String memberId = principal.getName();
        groupService.deleteGroup(groupId, memberId);

        return ResponseEntity.status(OK).body("accept");
    }

    /*
    @PostMapping("/groups/{groupId}/members")
    public ResponseEntity<GroupDto> groupMembers(@PathVariable Long groupId,
                                                 Principal principal) {
        String memberId = principal.getName();
        groupService.getGroupMembers(groupId, memberId);
    }
     */
    @GetMapping("/groups/{groupId}/members")
    public ResponseEntity<List<String>> getGroupMembers(@PathVariable Long groupId,
                                                        Principal principal) {
        String memberId = principal.getName();
        List<String> members = groupService.getGroupMembers(groupId, memberId);

        return ResponseEntity.status(OK).body(members);
    }

    @PostMapping("/groups/{groupId}/members")
    public ResponseEntity<Long> joinGroup(@PathVariable Long groupId,
                                          Principal principal) {
        String memberId = principal.getName();
        Long groupMemberId = groupService.joinGroup(groupId, memberId);

        return ResponseEntity.status(CREATED).body(groupMemberId);
    }

    @DeleteMapping("/groups/{groupId}/members")
    public ResponseEntity<String> exitGroup(@PathVariable Long groupId,
                                          Principal principal) {
        String memberId = principal.getName();
        groupService.exitGroup(groupId, memberId);

        return ResponseEntity.status(OK).body("accept");
    }

    @PostMapping("/groups/{groupId}/purposes")
    public ResponseEntity<Long> addPurpose(@RequestBody PurposeParam purposeParam,
                                           @PathVariable Long groupId) {
        Long purposeId = groupService.addPurpose(purposeParam, groupId);

        return ResponseEntity.status(CREATED).body(purposeId);
    }

    @DeleteMapping("/groups/{groupId}/purposes")
    public ResponseEntity<String> removePurpose(@RequestBody PurposeParam purposeParam,
                                                @PathVariable Long groupId,
                                                Principal principal) {
        String memberId = principal.getName();
        groupService.removePurpose(purposeParam, groupId, memberId);

        return ResponseEntity.status(OK).body("accept");
    }

    @PostMapping("/purposes")
    public ResponseEntity<Long> createPurpose(@RequestBody PurposeParam purposeParam) {
        Long purposeId = groupService.createPurpose(purposeParam);

        return ResponseEntity.status(CREATED).body(purposeId);
    }

    @GetMapping("/purposes")
    public ResponseEntity<Result<List<PurposeDto>>> getPurposes(@RequestParam(required = false) String content) {
        List<PurposeDto> purposes = groupService.getPurposes(content);
        Result<List<PurposeDto>> result = new Result<>();
        result.setData(purposes);

        return ResponseEntity.status(OK).body(result);
    }


}

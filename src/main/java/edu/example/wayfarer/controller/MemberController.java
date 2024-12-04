package edu.example.wayfarer.controller;

import edu.example.wayfarer.annotation.DeleteMemberOperation;
import edu.example.wayfarer.auth.util.SecurityUtil;
import edu.example.wayfarer.dto.member.MemberResponseDTO;
import edu.example.wayfarer.dto.responses.DeleteResponse;
import edu.example.wayfarer.entity.Member;
import edu.example.wayfarer.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;
    private final SecurityUtil securityUtil;

    @Operation(summary = "사용자 정보 조회")
    @GetMapping
    public ResponseEntity<MemberResponseDTO> readMember(){
        Member currentUser = securityUtil.getCurrentUser();
        return ResponseEntity.ok(memberService.read(currentUser.getEmail()));
    }

    @Operation(summary = "닉네임 수정")
    @PutMapping
    public ResponseEntity<MemberResponseDTO> updateMemberNickname(@RequestBody String newNickname){
        Member currentUser = securityUtil.getCurrentUser();
        return ResponseEntity.ok(memberService.updateNickname(newNickname, currentUser.getEmail()));
    }

    @Operation(summary = "프로필 사진 수정")
    @PutMapping("/profileImg")
    public ResponseEntity<MemberResponseDTO> updateProfileImg(@RequestParam("image") MultipartFile image ){
        Member currentUser = securityUtil.getCurrentUser();
        MemberResponseDTO updatedMember = memberService.updateImg(currentUser.getEmail(), image);
        return ResponseEntity.ok(updatedMember);
    }

    @DeleteMemberOperation
    @DeleteMapping
    public ResponseEntity<DeleteResponse> deleteMember(){
        Member currentUser = securityUtil.getCurrentUser();
        memberService.delete(currentUser.getEmail());
        DeleteResponse response = new DeleteResponse("삭제되었습니다.");
        return ResponseEntity.ok(response);
    }
}

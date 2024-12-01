package edu.example.wayfarer.controller;

import edu.example.wayfarer.auth.util.SecurityUtil;
import edu.example.wayfarer.dto.room.RoomResponseDTO;
import edu.example.wayfarer.dto.room.RoomUpdateDTO;
import edu.example.wayfarer.entity.Member;
import edu.example.wayfarer.service.MemberRoomService;
import edu.example.wayfarer.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
public class RoomController {
    private final RoomService roomService;
    private final MemberRoomService memberRoomService;
    private final SecurityUtil securityUtil;

    @Operation(summary = "단일 방 정보 조회")
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponseDTO> readRoom(@PathVariable String roomId) {
        return ResponseEntity.ok(roomService.read(roomId));
    }

    @Operation(summary = "방 정보 수정")
    @PutMapping
    public ResponseEntity<RoomResponseDTO> updateRoom(@RequestBody RoomUpdateDTO roomUpdateDTO) {
        // 로그인한 사용자가 해당 방의 방장이 맞는지 아닌지 확인
        Member currentUser = securityUtil.getCurrentUser();
        RoomUpdateDTO updatedDTO = new RoomUpdateDTO(
                roomUpdateDTO.roomId(),
                roomUpdateDTO.title(),
                roomUpdateDTO.country(),
                roomUpdateDTO.startDate(),
                roomUpdateDTO.endDate(),
                currentUser.getEmail()
        );
        return ResponseEntity.ok(roomService.update(updatedDTO));
    }

    @Operation(summary = "방 삭제 (방장만 가능)")
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Map<String, String>> deleteRoom(@PathVariable String roomId) {
        Member currentUser = securityUtil.getCurrentUser();
        roomService.delete(currentUser, roomId);
        return ResponseEntity.ok(Map.of("message", "삭제되었습니다."));
    }

    @Operation(summary = "방 퇴장")
    @DeleteMapping("/leave/{roomId}")
    public ResponseEntity<Map<String, String>> leaveRoom(@PathVariable String roomId) {
        Member currentUser = securityUtil.getCurrentUser();
        memberRoomService.delete(currentUser, roomId);
        return ResponseEntity.ok(Map.of("message", "퇴장하였습니다."));
    }

}

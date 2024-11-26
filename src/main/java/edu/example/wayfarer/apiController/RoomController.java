package edu.example.wayfarer.apiController;

import edu.example.wayfarer.dto.room.RoomResponseDTO;
import edu.example.wayfarer.dto.room.RoomUpdateDTO;
import edu.example.wayfarer.service.MemberRoomService;
import edu.example.wayfarer.service.RoomService;
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

    //단일 방 정보 조회
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponseDTO> readRoom(@PathVariable("roomId") String roomId) {
        return ResponseEntity.ok(roomService.read(roomId));
    }

    // 방 정보 수정
    @PutMapping
    public ResponseEntity<RoomResponseDTO> updateRoom(@RequestBody RoomUpdateDTO roomUpdateDTO) {
        return ResponseEntity.ok(roomService.update(roomUpdateDTO));
    }

    // 방 삭제 (방장만 가능)
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Map<String, String>> deleteRoom(@PathVariable("roomId") String roomId) {
        roomService.delete(roomId);
        return ResponseEntity.ok(Map.of("message", "삭제되었습니다."));
    }

    // 방 퇴장
//    @DeleteMapping("/leave")
//    public ResponseEntity<Map<String, String>> leaveRoom(@RequestParam("roomId") String roomId) {
//
//    }

}

package edu.example.wayfarer.api_controller;

import edu.example.wayfarer.dto.memberRoom.MemberRoomRequestDTO;
import edu.example.wayfarer.dto.room.RoomListDTO;
import edu.example.wayfarer.dto.room.RoomRequestDTO;
import edu.example.wayfarer.dto.room.RoomResponseDTO;
import edu.example.wayfarer.service.MemberRoomService;
import edu.example.wayfarer.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
public class MainController {

    private final RoomService roomService;
    private final MemberRoomService memberRoomService;

    // 방 생성
    @PostMapping
    public ResponseEntity<RoomResponseDTO> createRoom(@RequestBody RoomRequestDTO roomRequestDTO) {
        return ResponseEntity.ok(roomService.create(roomRequestDTO));
    }

    // 방리스트 조회
    @GetMapping("/list")
    public ResponseEntity<List<RoomListDTO>> getListByEmail() {
        List<RoomListDTO> rooms = memberRoomService.listByEmail();
        return ResponseEntity.ok(rooms);
    }

    // 방 입장
    @PostMapping("/join")
    public ResponseEntity<Map<String,String>> createMemberRoom(@RequestBody MemberRoomRequestDTO memberRoomRequestDTO) {
        memberRoomService.create(memberRoomRequestDTO);
        return ResponseEntity.ok(Map.of("message", "방에 입장했습니다."));
    }

}

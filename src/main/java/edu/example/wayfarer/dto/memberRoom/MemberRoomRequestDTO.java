package edu.example.wayfarer.dto.memberRoom;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberRoomRequestDTO {
    private String roomId;
    private String roomCode;
    private String email; // 임시

}

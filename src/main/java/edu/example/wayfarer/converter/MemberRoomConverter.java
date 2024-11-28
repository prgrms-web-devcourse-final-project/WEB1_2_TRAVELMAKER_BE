package edu.example.wayfarer.converter;

import edu.example.wayfarer.dto.memberRoom.MemberRoomResponseDTO;
import edu.example.wayfarer.entity.MemberRoom;

public class MemberRoomConverter {

    public static MemberRoomResponseDTO toMemberRoomResponseDTO(MemberRoom memberRoom) {
        return new MemberRoomResponseDTO(
                memberRoom.getMemberRoomId(),
                memberRoom.getRoom().getRoomId(),
                memberRoom.getMember().getEmail(),
                memberRoom.getColor(),
                memberRoom.getJoinDate()
        );
    }

}

package edu.example.wayfarer.service;

import edu.example.wayfarer.dto.memberRoom.MemberRoomRequestDTO;
import edu.example.wayfarer.dto.memberRoom.MemberRoomResponseDTO;

public interface MemberRoomService {

    public MemberRoomResponseDTO create(MemberRoomRequestDTO memberRoomRequestDTO);

    public void delete(String email, String roomId);

}

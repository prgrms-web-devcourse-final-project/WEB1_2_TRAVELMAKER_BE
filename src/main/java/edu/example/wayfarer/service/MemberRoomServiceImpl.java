package edu.example.wayfarer.service;

import edu.example.wayfarer.dto.memberRoom.MemberRoomRequestDTO;
import edu.example.wayfarer.dto.memberRoom.MemberRoomResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberRoomServiceImpl implements MemberRoomService {
    @Override
    public MemberRoomResponseDTO create(MemberRoomRequestDTO memberRoomRequestDTO) {

        return null;
    }

    @Override
    public void delete(String email, String roomId) {

    }
}

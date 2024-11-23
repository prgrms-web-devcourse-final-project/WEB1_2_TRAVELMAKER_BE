package edu.example.wayfarer.service;

import edu.example.wayfarer.dto.memberRoom.MemberRoomRequestDTO;
import edu.example.wayfarer.dto.memberRoom.MemberRoomResponseDTO;
import edu.example.wayfarer.entity.Member;
import edu.example.wayfarer.entity.MemberRoom;
import edu.example.wayfarer.entity.Room;
import edu.example.wayfarer.entity.enums.Color;
import edu.example.wayfarer.exception.MemberRoomException;
import edu.example.wayfarer.repository.MemberRepository;
import edu.example.wayfarer.repository.MemberRoomRepository;
import edu.example.wayfarer.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberRoomServiceImpl implements MemberRoomService {

    private final MemberRoomRepository memberRoomRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

    /*
    create 설명
    1. 제일 먼저 해당 방을 찾습니다. (찾을 수 없을 시 존재 X 예외)
    2. RoomId와 RoomCode가 일치하는지 검증합니다. (다를 시 INVALID_ROOMCODE 예외)
    3. (로그인한 상태니 당연히 currentUser로 받을 수 있어야 하지만 아직은 로그인 기능이 없으니 member를 찾는 걸로 임시대체)
    4. 사용자가 해당 방에 이미 들어와있는 상태인지 아닌지 확인 (이미 들어와 있을 경우 DUPLICATED_MEMBER 예외)
    5. Color enum을 차례대로 순회하며, 아무도 안 쓰고 있는 첫 번째 color를 찾아 해당 사용자에게 할당합니다.
        (사용 가능한 색상이 없을 경우 정원 초과로 간주, OVER_CAPACITY 예외)
     */
    @Override
    public MemberRoomResponseDTO create(MemberRoomRequestDTO memberRoomRequestDTO) {
        Room room = roomRepository.findById(memberRoomRequestDTO.getRoomId())
                .orElseThrow(()-> new NoSuchElementException("해당 방이 존재하지 않습니다."));

        // MemberRoomRequestDTO에 있는 roomId와 roomCode가 맞는지 확인
        if(!room.getRoomCode().equals(memberRoomRequestDTO.getRoomCode())) {
            System.out.println(room.getRoomCode());
            System.out.println(memberRoomRequestDTO.getRoomCode());
            throw MemberRoomException.INVALID_ROOMCODE.get();
        }

        // Member currentUser 임시 대체
        Member member = memberRepository.findById(memberRoomRequestDTO.getEmail())
                .orElseThrow(()-> new NoSuchElementException("해당 유저는 없는 유저입니다."));

        if(memberRoomRepository.existsByMember_Email(memberRoomRequestDTO.getEmail())) { // -> 나중엔 뭐 currentUser.getEmail() 이런식으로 받겠죠?
            throw MemberRoomException.DUPLICATED_MEMBER.get();
        }

        // Color 순회하여 사용 가능한 Color 찾기
        Color assignedColor = null;
        Color[] colors = Color.values();
        for (int i = 1; i< colors.length; i++) {
            Color color = colors[i];
            if(!memberRoomRepository.existsByRoom_RoomIdAndColor(memberRoomRequestDTO.getRoomId(), color)){
                assignedColor = color;
                break;  // 사용 가능한 첫 번째 Color 발견
            }
        }

        // 할당할 색이 없을시, 정원 초과로 간주 -> 예외 발생
        if(assignedColor == null) {
            throw MemberRoomException.OVER_CAPACITY.get();
        }

        MemberRoom memberRoom = MemberRoom.builder()
                .room(room)
                .member(member)
                .color(assignedColor).build();

        memberRoomRepository.save(memberRoom);

        return new MemberRoomResponseDTO(memberRoom);
    }

    @Override
    public void delete(String email, String roomId) {

    }
}

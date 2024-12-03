package edu.example.wayfarer.service;

import edu.example.wayfarer.converter.MemberRoomConverter;
import edu.example.wayfarer.converter.RoomConverter;
import edu.example.wayfarer.dto.memberRoom.MemberRoomRequestDTO;
import edu.example.wayfarer.dto.memberRoom.MemberRoomResponseDTO;
import edu.example.wayfarer.dto.room.RoomListDTO;
import edu.example.wayfarer.entity.Member;
import edu.example.wayfarer.entity.MemberRoom;
import edu.example.wayfarer.entity.Room;
import edu.example.wayfarer.entity.enums.Color;
import edu.example.wayfarer.exception.MemberException;
import edu.example.wayfarer.exception.MemberRoomException;
import edu.example.wayfarer.exception.RoomException;
import edu.example.wayfarer.repository.MemberRepository;
import edu.example.wayfarer.repository.MemberRoomRepository;
import edu.example.wayfarer.repository.RoomRepository;
import edu.example.wayfarer.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberRoomServiceImpl implements MemberRoomService {

    private final MemberRoomRepository memberRoomRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final ScheduleRepository scheduleRepository;

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
    public MemberRoomResponseDTO create(MemberRoomRequestDTO memberRoomRequestDTO, String email) {
        Room room = roomRepository.findById(memberRoomRequestDTO.roomId())
                .orElseThrow(MemberRoomException.ROOM_NOT_FOUND::get);

        // MemberRoomRequestDTO에 있는 roomId와 roomCode가 맞는지 확인
        if(!room.getRoomCode().equals(memberRoomRequestDTO.roomCode())) {
            throw MemberRoomException.INVALID_ROOMCODE.get();
        }

        Member currentUser = memberRepository.findByEmail(email)
                .orElseThrow(MemberException.NOT_FOUND::get);

        boolean memberExistsInRoom = memberRoomRepository.findAllByRoomRoomId(memberRoomRequestDTO.roomId())
                .stream()
                .anyMatch(existingMemberRoom -> existingMemberRoom.getMember().getEmail().equals(currentUser.getEmail()));

        if(memberExistsInRoom) {
            throw MemberRoomException.DUPLICATED_MEMBER.get();
        }

        // Color 순회하여 사용 가능한 Color 찾기
        Color assignedColor = Stream.of(Color.values())
                .skip(1)
                .filter(color -> !memberRoomRepository.existsByRoomRoomIdAndColor(memberRoomRequestDTO.roomId(), color))
                .findFirst()
                .orElseThrow(MemberRoomException.OVER_CAPACITY::get);

        MemberRoom memberRoom = MemberRoom.builder()
                .room(room)
                .member(currentUser)
                .color(assignedColor).build();

        room.getMemberRooms().add(memberRoom);

        memberRoomRepository.save(memberRoom);

        return MemberRoomConverter.toMemberRoomResponseDTO(memberRoom);
    }

    /* delete 설명 (퇴장)
    1. 방을 퇴장하려는 사용자가 방장이 아닐 경우, memberRoom 데이터 하나만 삭제
    2. 방을 퇴장하려는 사용자가 방장일 경우, 방장은 그 다음 Color인 사람으로 바뀌고 memberRoom 데이터 하나만 삭제
     */
    @Override
    public void delete(Member member, String roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new NoSuchElementException("해당 방이 존재하지 않습니다."));

        if(room.getHostEmail().equals(member.getEmail())) {
            hostExit(member, room);

        }else {
            MemberRoom memberRoom = memberRoomRepository.findByMemberEmailAndRoomRoomId(member.getEmail(), roomId)
                    .orElseThrow(()-> new NoSuchElementException("이미 없는 회원입니다."));
            memberRoomRepository.delete(memberRoom);
        }

    }

    // 해당 방에 참가하고 있는 참여자들을 볼 수 있는 리스트
    @Override
    public List<MemberRoomResponseDTO> listByRoomId(String roomId) {
        // 1. 방에 속한 모든 MemberRoom 조회
        List<MemberRoom> memberRooms = memberRoomRepository.findAllByRoomRoomId(roomId);

        // 2. MemberRoom 엔티티를 MemberRoomResponseDTO로 변환
        return memberRooms.stream()
                .map(MemberRoomConverter::toMemberRoomResponseDTO)
                .toList();
    }

    @Override
    public List<RoomListDTO> listByEmail(Member member) {
        try {
            List<MemberRoom> memberRooms = memberRoomRepository.findAllByMemberEmail(member.getEmail());

            List<RoomListDTO> roomListDTOS = memberRooms.stream()
                    .map(memberRoom -> {
                        Room room = roomRepository.findById(memberRoom.getRoom().getRoomId())
                                .orElseThrow(RoomException.DOESNT_EXIST::get);
                        return RoomConverter.toRoomListDTO(room);
                    })
                    .collect(Collectors.toList());

            return roomListDTOS;
        }catch (Exception e) {
            throw RoomException.DOESNT_EXIST.get();
        }

    }

    protected void hostExit(Member member, Room room){
        // 현재 방장의 MemberRoom 조회
        MemberRoom currentHost = memberRoomRepository.findByMemberEmailAndRoomRoomId(member.getEmail(), room.getRoomId())
                .orElseThrow(() -> new NoSuchElementException("이미 없는 회원입니다.")); // 여기 사실 방장이 없으면 안되는 건데..

        // 방의 다른 멤버 조회
        List<MemberRoom> remainingMembers = memberRoomRepository.findAllByRoomRoomId(room.getRoomId()).stream()
                .filter(memberRoom -> !memberRoom.getMember().getEmail().equals(room.getHostEmail()))   // 방장 제외
                .toList();

        if(remainingMembers.isEmpty()) {
            memberRoomRepository.delete(currentHost);
            scheduleRepository.deleteByRoomId(room.getRoomId());
            roomRepository.delete(room);
        }else {
            // 다음 방장 선정: 남은 멤버 중 Color 인덱스가 가장 작은 사람
            MemberRoom nextHost = memberRoomRepository.findAllByRoomRoomId(room.getRoomId()).stream()
                    .filter(memberRoom -> !memberRoom.getMember().getEmail().equals(room.getHostEmail())) // 방장 제외
                    .min(Comparator.comparingInt(memberRoom -> memberRoom.getColor().ordinal())) // Color 인덱스 기준 정렬
                    .orElseThrow(() -> new IllegalStateException("방에 남아 있는 회원이 없어 방을 유지할 수 없습니다."));

            // 새로운 방장 설정
            room.setHostEmail(nextHost.getMember().getEmail());
            roomRepository.save(room);

            // 기존 방장의 MemberRoom 삭제
            memberRoomRepository.delete(currentHost);
        }
    }


}

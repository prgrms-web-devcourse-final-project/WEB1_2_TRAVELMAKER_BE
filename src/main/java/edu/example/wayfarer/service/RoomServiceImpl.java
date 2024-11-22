package edu.example.wayfarer.service;

import edu.example.wayfarer.dto.room.RoomListDTO;
import edu.example.wayfarer.dto.room.RoomRequestDTO;
import edu.example.wayfarer.dto.room.RoomResponseDTO;
import edu.example.wayfarer.dto.room.RoomUpdateDTO;
import edu.example.wayfarer.entity.Member;
import edu.example.wayfarer.entity.MemberRoom;
import edu.example.wayfarer.entity.Room;
import edu.example.wayfarer.entity.Schedule;
import edu.example.wayfarer.entity.enums.Color;
import edu.example.wayfarer.entity.enums.PlanType;
import edu.example.wayfarer.repository.MemberRepository;
import edu.example.wayfarer.repository.MemberRoomRepository;
import edu.example.wayfarer.repository.RoomRepository;
import edu.example.wayfarer.repository.ScheduleRepository;
import edu.example.wayfarer.util.RandomStringGenerator;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final MemberRoomRepository memberRoomRepository;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public RoomResponseDTO create(RoomRequestDTO roomRequestDTO) {
        // 여행 끝 날짜가 더 나중이 맞는지 확인
        if(roomRequestDTO.getStartDate().isAfter(roomRequestDTO.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        // room 저장
        Room room = modelMapper.map(roomRequestDTO, Room.class);
        System.out.println("Host email: " + room.getHostEmail());

        // roomId 생성하고 중복 확인
        String roomId;
        do{
            room.generateRoomId();
            roomId = room.getRoomId();
        }while (roomRepository.existsById(roomId));
        Room savedRoom = roomRepository.save(room);
        System.out.println("Saved Room ID: " + savedRoom.getRoomId());
        System.out.println("Host email: " + room.getHostEmail());


        //memberRoom 저장
        // 방장을 찾는다
        Member foundMember = memberRepository.findById(room.getHostEmail()).orElseThrow();
        // Color enum을 배열화
        Color[] colors = Color.values();
        // memberRoom을 build
        MemberRoom memberRoom = MemberRoom.builder()
                .member(foundMember)
                .room(savedRoom)
                .color(colors[1]).build();
        memberRoomRepository.save(memberRoom);

        // schedule 저장
        List<Schedule> schedules = new ArrayList<>();
        LocalDate currentDate = roomRequestDTO.getStartDate();

        while(!currentDate.isAfter(roomRequestDTO.getEndDate())) {
            for(PlanType planType : PlanType.values()) {
                schedules.add(Schedule.builder()
                        .room(savedRoom)
                        .planType(planType)
                        .date(currentDate)
                        .build()
                );

            }
            currentDate = currentDate.plusDays(1);
        }
        for (Schedule schedule : schedules) {
            System.out.println("Schedule: date=" + schedule.getDate() +
                    ", planType=" + schedule.getPlanType() +
                    ", roomId=" + (schedule.getRoom() != null ? schedule.getRoom().getRoomId() : "null"));

            scheduleRepository.save(schedule);
        }

        return new RoomResponseDTO(savedRoom);
    }

    @Override
    public RoomResponseDTO read(String roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new NoSuchElementException("해당 방이 존재하지 않습니다."));
        return new RoomResponseDTO(room);
    }

    @Override
    public RoomResponseDTO update(RoomUpdateDTO roomUpdateDTO) {
        Room room = roomRepository.findById(roomUpdateDTO.getRoomId())
                .orElseThrow(()-> new NoSuchElementException("해당 방이 존재하지 않습니다."));

        room.changeCountry(roomUpdateDTO.getCountry());
        room.changeTitle(roomUpdateDTO.getTitle());

        return new RoomResponseDTO(roomRepository.save(room));
    }

    @Override
    public void delete(String roomId) {

    }

    @Override
    public List<RoomListDTO> getList() {
        return List.of();
    }
}

package edu.example.wayfarer.service;

import edu.example.wayfarer.dto.marker.MarkerListDTO;
import edu.example.wayfarer.dto.marker.MarkerRequestDTO;
import edu.example.wayfarer.dto.marker.MarkerResponseDTO;
import edu.example.wayfarer.dto.marker.MarkerUpdateDTO;
import edu.example.wayfarer.entity.Member;
import edu.example.wayfarer.entity.MemberRoom;
import edu.example.wayfarer.entity.Room;
import edu.example.wayfarer.entity.Schedule;
import edu.example.wayfarer.entity.enums.PlanType;
import edu.example.wayfarer.repository.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MarkerServiceTests {
    @Autowired
    private MarkerService markerService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MarkerRepository markerRepository;
    @Autowired
    private MemberRoomRepository memberRoomRepository;


    @Test
    @Order(1)
    public void testData() { // 테스트용 데이터 생성
        // 임의의 Member 생성
        if( !memberRepository.existsById("member1@abc.com") ) {
            Member member = Member.builder()
                    .email("member1@abc.com")
                    .nickname("멤버1")
                    .profileImage("image1.png")
                    .password(passwordEncoder.encode("1111"))
                    .role("ROLE_USER")
                    .build();

            memberRepository.save(member);
        }

        // 임의의 Room 생성
        if( !roomRepository.existsById("abc1")) {
            LocalDateTime time1 = LocalDateTime.now();
            LocalDateTime time2 = time1.plusDays(2);
            Room room = Room.builder()
                    .roomId("abc1")
                    .title("테스트용 Room")
                    .country("대한민국")
                    .startDate(time1)
                    .endDate(time2)
                    .roomCode("abc1")
                    .hostEmail("member1@abc.com")
                    .build();

            Room savedRoom = roomRepository.save(room);

            LocalDate start = savedRoom.getStartDate().toLocalDate();
            LocalDate end = savedRoom.getEndDate().toLocalDate();

            // 임의의 스케쥴 생성
            List<Schedule> schedules = new ArrayList<>();

            for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                for (int i = 0; i<2; i++) {
                    Schedule schedule = Schedule.builder()
                            .room(savedRoom)
                            .date(date)
                            .planType(PlanType.values()[i])
                            .build();

                    schedules.add(schedule);
                }
            }
            scheduleRepository.saveAll(schedules);

            MemberRoom memberRoom = MemberRoom.builder()
                    .room(savedRoom)
                    .member(memberRepository.findById("member1@abc.com").get())
                    .color("#FFFF00")
                    .joinDate(LocalDateTime.now())
                    .build();
            memberRoomRepository.save(memberRoom);

        }
    }

    @Test
    @Order(2)
    public void testCreateMarker() {
        for (int i=0; i<3; i++) {
            MarkerRequestDTO markerRequestDTO = new MarkerRequestDTO();
            markerRequestDTO.setEmail("member1@abc.com");
            markerRequestDTO.setScheduleId(1L);
            markerRequestDTO.setLat(37.552);
            markerRequestDTO.setLng(126.988);

            MarkerResponseDTO markerResponseDTO = markerService.create(markerRequestDTO);
            System.out.println(markerResponseDTO);
        }
    }

    @Test
    @Order(3)
    public void testReadMarker() {
        Long markerId = 1L;
        MarkerResponseDTO markerResponseDTO = markerService.read(markerId);
        System.out.println(markerResponseDTO);
    }

    @Test
    @Order(4)
    public void testReadMarkers(){
        Long scheduleId = 1L;
        List<MarkerResponseDTO> markerResponseDTOS = markerService.getListBySchedule(scheduleId);
        System.out.println(markerResponseDTOS);
    }

    @Test
    @Order(5)
    public void testReadAllMarkers() {
        String roomId = "abc1";
        List<MarkerListDTO> markerListDTOS = markerService.getListByRoom(roomId);
        System.out.println(markerListDTOS);
    }

    @Test
    @Order(6)
    public void testUpdateMarkerTrue() {
        MarkerUpdateDTO markerUpdateDTO = new MarkerUpdateDTO();
        markerUpdateDTO.setMarkerId(1L);
        markerUpdateDTO.setConfirm(true);
        markerUpdateDTO.setEmail("member1@abc.com");

        System.out.println(markerService.update(markerUpdateDTO));
    }

    @Test
    @Order(7)
    public void testUpdateMarkerFalse() {
        MarkerUpdateDTO markerUpdateDTO = new MarkerUpdateDTO();
        markerUpdateDTO.setMarkerId(1L);
        markerUpdateDTO.setConfirm(false);
        markerUpdateDTO.setEmail("member1@abc.com");

        System.out.println(markerService.update(markerUpdateDTO));
    }

    @Test
    @Order(8)
    public void testDeleteMarker() {
        Long markerId = 1L;
        markerService.delete(markerId);

    }


}

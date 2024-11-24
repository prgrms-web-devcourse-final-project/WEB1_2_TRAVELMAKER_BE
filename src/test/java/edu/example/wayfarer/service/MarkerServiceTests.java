package edu.example.wayfarer.service;

import edu.example.wayfarer.dto.marker.MarkerListDTO;
import edu.example.wayfarer.dto.marker.MarkerRequestDTO;
import edu.example.wayfarer.dto.marker.MarkerResponseDTO;
import edu.example.wayfarer.dto.marker.MarkerUpdateDTO;
import edu.example.wayfarer.entity.*;
import edu.example.wayfarer.entity.enums.Color;
import edu.example.wayfarer.entity.enums.Days;
import edu.example.wayfarer.entity.enums.PlanType;
import edu.example.wayfarer.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    private MemberRoomRepository memberRoomRepository;
    @Autowired
    private ScheduleItemService scheduleItemService;


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
                    .startDate(time1.toLocalDate())
                    .endDate(time2.toLocalDate())
                    .roomCode("abc1")
                    .hostEmail("member1@abc.com")
                    .build();

            Room savedRoom = roomRepository.save(room);

            LocalDate start = savedRoom.getStartDate();
            LocalDate end = savedRoom.getEndDate();

            // 임의의 스케쥴 생성
            List<Schedule> schedules = new ArrayList<>();

            long daysBetween = ChronoUnit.DAYS.between(start, end)+1;
            Days[] days = Days.values();
            for (int i = 0; i < daysBetween; i++) {
                for (int j = 0; j<2; j++) {
                    Schedule schedule = Schedule.builder()
                            .room(savedRoom)
                            .date(days[i])
                            .planType(PlanType.values()[j])
                            .build();

                    schedules.add(schedule);
                }
            }
            scheduleRepository.saveAll(schedules);
            Member foundMember = memberRepository.findById("member1@abc.com")
                    .orElseThrow(() -> new RuntimeException("Member not found"));

            MemberRoom memberRoom = MemberRoom.builder()
                    .room(savedRoom)
                    .member(foundMember)
                    .color(Color.BLUE)
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

            System.out.println("2. 마커 생성 테스트" + i);
            System.out.println(markerResponseDTO);
        }
    }

    @Test
    @Order(3)
    public void testReadMarker() {
        Long markerId = 1L;
        MarkerResponseDTO markerResponseDTO = markerService.read(markerId);

        System.out.println("3. 마커 조회 테스트");
        System.out.println(markerResponseDTO);
    }

    @Test
    @Order(4)
    public void testReadMarkers(){
        Long scheduleId = 1L;
        List<MarkerResponseDTO> markerResponseDTOS = markerService.getListBySchedule(scheduleId);

        System.out.println("4. 마커 리스트 조회(스케쥴단위) 테스트");
        System.out.println(markerResponseDTOS);
    }

    @Test
    @Order(5)
    public void testReadAllMarkers() {
        String roomId = "abc1";
        List<MarkerListDTO> markerListDTOS = markerService.getListByRoom(roomId);

        System.out.println("5. 마커 리스트 조회(룸단위) 테스트");
        System.out.println(markerListDTOS);
    }

    @Test
    @Order(6)
    public void testUpdateMarkerTrue() {
        MarkerUpdateDTO markerUpdateDTO = new MarkerUpdateDTO();
        markerUpdateDTO.setMarkerId(1L);
        markerUpdateDTO.setConfirm(true);
        markerUpdateDTO.setEmail("member1@abc.com");

        System.out.println("6. 마커 확정 테스트");
        System.out.println(markerService.update(markerUpdateDTO));
        System.out.println(scheduleItemService.read(1L));
    }

    @Test
    @Order(7)
    public void testUpdateMarkerFalse() {
        MarkerUpdateDTO markerUpdateDTO = new MarkerUpdateDTO();
        markerUpdateDTO.setMarkerId(1L);
        markerUpdateDTO.setConfirm(false);
        markerUpdateDTO.setEmail("member1@abc.com");

        System.out.println("7. 마커 확정 취소 테스트");
        System.out.println(markerService.update(markerUpdateDTO));
//        System.out.println(scheduleItemService.read(1L));
    }

    @Test
    @Order(8)
    public void testDeleteMarker() {
        Long markerId = 3L;
        markerService.delete(markerId);
    }

    @Test
    @Order(9)
    public void testDeleteItem() {
//        Long markerId = 1L;
//
//        Marker foundMarker = markerRepository.findById(markerId).orElseThrow(RuntimeException::new);
//
//        if(foundMarker.getScheduleItem() != null ) {
//            foundMarker.changeScheduleItem(null);
//            markerRepository.save(foundMarker);
//        }

//        ScheduleItem scheduleItem = scheduleItemRepository.findByMarker_MarkerId(markerId)
//                .orElseThrow(() -> new RuntimeException("ScheduleItem not found"));
//        System.out.println(ScheduleItemConverter.toScheduleItemResponseDTO(scheduleItem));
//
//        // 엔티티 상태 확인
//        boolean isManaged = entityManager.contains(scheduleItem);
//        System.out.println("Is scheduleItem managed: " + isManaged);
//        System.out.println("Transaction active: " + TransactionSynchronizationManager.isActualTransactionActive());
//
//        scheduleItemRepository.deleteById(scheduleItem.getScheduleItemId());

    }

}

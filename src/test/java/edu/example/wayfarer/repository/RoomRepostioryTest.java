package edu.example.wayfarer.repository;

import edu.example.wayfarer.entity.Room;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class RoomRepostioryTest {

    @Autowired
    private RoomRepository roomRepository;

    @BeforeEach
    void setup(){
        roomRepository.deleteAll(); // 기존 방 삭제

    }

    @Test
    @DisplayName("방 생성 테스트")
    @Commit
    void testCreateRoom(){

        Room room = Room.builder()
                .country("Japan")
                .hostEmail("example@a.com")
                .startDate(LocalDate.of(2024, 12, 1))
                .endDate(LocalDate.of(2024, 12, 4))
                .title("공주들")
                .build();

        Room savedRoom = roomRepository.save(room);
        assertNotNull(room.getRoomId());
        assertThat(savedRoom).isNotNull();

    }




}

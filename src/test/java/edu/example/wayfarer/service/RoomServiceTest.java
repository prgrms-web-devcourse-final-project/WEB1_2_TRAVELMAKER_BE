package edu.example.wayfarer.service;

import edu.example.wayfarer.dto.room.RoomRequestDTO;
import edu.example.wayfarer.dto.room.RoomResponseDTO;
import edu.example.wayfarer.entity.Member;
import edu.example.wayfarer.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class RoomServiceTest {
    @Autowired
    private RoomService roomService;

    @Test
    @Transactional
    @Commit
    public void testCreateRoom() {
        RoomRequestDTO roomRequestDTO = new RoomRequestDTO();
        roomRequestDTO.setTitle("여행!!!!");
        roomRequestDTO.setCountry("마카오");
        roomRequestDTO.setStartDate(LocalDate.of(2025,1,6));
        roomRequestDTO.setEndDate(LocalDate.of(2025,1,10));
        roomRequestDTO.setHostEmail("aa@aa.com");

        RoomResponseDTO result = roomService.create(roomRequestDTO);
        assertNotNull(result);
    }

    @Test
    public void testReadRoom(){
        String roomId = "xu688Ljt";

        RoomResponseDTO result = roomService.read(roomId);
        assertNotNull(result);
        System.out.println(result);
    }
}

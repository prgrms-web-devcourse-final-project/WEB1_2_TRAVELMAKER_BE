package edu.example.wayfarer.service;

import edu.example.wayfarer.dto.memberRoom.MemberRoomRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class MemberRoomServiceTests {

    @Autowired
    private MemberRoomService memberRoomService;

    @Test
    @Transactional
    @Commit
    public void testCreateMemberRoom() {
        MemberRoomRequestDTO memberRoomRequestDTO = new MemberRoomRequestDTO();
        memberRoomRequestDTO.setRoomId("xu688Ljt");
        memberRoomRequestDTO.setRoomCode("xuA19CYH");
        memberRoomRequestDTO.setEmail("jj@jj.com");

        memberRoomService.create(memberRoomRequestDTO);
    }

}

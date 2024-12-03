package edu.example.wayfarer.service;

import edu.example.wayfarer.dto.member.MemberNicknameUpdateDTO;
import edu.example.wayfarer.dto.member.MemberResponseDTO;
import edu.example.wayfarer.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class MemberServiceTests {

    @Autowired
    private MemberService memberService;

    @Test
    public void testRead(){
        String email = "jasminekim1009@gmail.com";
        MemberResponseDTO member = memberService.read(email);
        System.out.println(member);
    }

    @Test
    @Transactional
    @Commit
    public void testUpdateNickname(){
        MemberNicknameUpdateDTO memberNicknameUpdateDTO = new MemberNicknameUpdateDTO(
                "aa@aa.com",
                "수정된 a의 이름"
        );
        MemberResponseDTO newNic = memberService.updateNickname(memberNicknameUpdateDTO);
        System.out.println(newNic);
    }

    // testUpdateImg

    @Test
    @Transactional
    @Commit
    public void testDelete(){
        String email = "jasminekim1009@gmail.com";
        memberService.delete(email);
    }
}

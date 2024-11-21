package edu.example.wayfarer.repository;

import edu.example.wayfarer.entity.Member;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
//@TestPropertySource(locations = ["classpath:application-test.properties"])
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MarkerRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @Order(1)
    public void createMember() {
        Member member = Member.builder()
                .email("member1@abc.com")
                .nickname("멤버1")
                .profileImage("image1.png")
                .password(passwordEncoder.encode("1111"))
                .role("ROLE_USER")
                .build();

        memberRepository.save(member);
    }
}

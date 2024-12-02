package edu.example.wayfarer.service;

import edu.example.wayfarer.converter.MemberConverter;
import edu.example.wayfarer.dto.member.MemberResponseDTO;
import edu.example.wayfarer.entity.Member;
import edu.example.wayfarer.exception.MemberException;
import edu.example.wayfarer.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public MemberResponseDTO.MemberPreviewDTO read(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberException.NOT_FOUND::get);
        return MemberConverter.toMemberPreviewDTO(member);
    }


}

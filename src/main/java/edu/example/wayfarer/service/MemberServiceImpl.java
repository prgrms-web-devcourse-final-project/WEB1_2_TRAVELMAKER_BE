package edu.example.wayfarer.service;

import edu.example.wayfarer.converter.MemberConverter;
import edu.example.wayfarer.dto.member.MemberImgUpdateDTO;
import edu.example.wayfarer.dto.member.MemberResponseDTO;
import edu.example.wayfarer.dto.member.MemberNicknameUpdateDTO;
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
    public MemberResponseDTO read(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberException.NOT_FOUND::get);
        return MemberConverter.toMemberResponseDTO(member);
    }

    @Override
    public MemberResponseDTO updateNickname(MemberNicknameUpdateDTO memberUpdateDTO) {
        Member member = memberRepository.findByEmail(memberUpdateDTO.email())
                .orElseThrow(MemberException.NOT_FOUND::get);
        member.changeNickname(memberUpdateDTO.nickname());
        return MemberConverter.toMemberResponseDTO(member);
    }

    @Override
    public MemberResponseDTO updateImg(MemberImgUpdateDTO memberPicUpdateDTO) {
        Member member = memberRepository.findByEmail(memberPicUpdateDTO.email())
                .orElseThrow(MemberException.NOT_FOUND::get);
        member.changeImage(memberPicUpdateDTO.filename());

        return MemberConverter.toMemberResponseDTO(member);
    }


}

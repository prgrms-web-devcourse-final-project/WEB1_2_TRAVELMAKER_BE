package edu.example.wayfarer.service;

import edu.example.wayfarer.dto.member.MemberImgUpdateDTO;
import edu.example.wayfarer.dto.member.MemberResponseDTO;
import edu.example.wayfarer.dto.member.MemberNicknameUpdateDTO;

public interface MemberService {

    MemberResponseDTO read(String email);

    MemberResponseDTO updateNickname(MemberNicknameUpdateDTO memberUpdateDTO);

    MemberResponseDTO updateImg(MemberImgUpdateDTO memberPicUpdateDTO);

    void delete(String email);
}

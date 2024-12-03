package edu.example.wayfarer.service;

import edu.example.wayfarer.dto.member.MemberImgUpdateDTO;
import edu.example.wayfarer.dto.member.MemberResponseDTO;

public interface MemberService {

    MemberResponseDTO read(String email);

    MemberResponseDTO updateNickname(String newNickname, String email);

    MemberResponseDTO updateImg(MemberImgUpdateDTO memberPicUpdateDTO);

    void delete(String email);
}

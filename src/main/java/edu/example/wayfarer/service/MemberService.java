package edu.example.wayfarer.service;

import edu.example.wayfarer.dto.member.MemberResponseDTO;

public interface MemberService {

    MemberResponseDTO.MemberPreviewDTO read(String email);

}

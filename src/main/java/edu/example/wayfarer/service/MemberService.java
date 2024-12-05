package edu.example.wayfarer.service;

import edu.example.wayfarer.dto.member.MemberResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {

    MemberResponseDTO read(String email);

    MemberResponseDTO updateNickname(String newNickname, String email);

    MemberResponseDTO updateImg(String email, MultipartFile newImage);

    void delete(String email);
}

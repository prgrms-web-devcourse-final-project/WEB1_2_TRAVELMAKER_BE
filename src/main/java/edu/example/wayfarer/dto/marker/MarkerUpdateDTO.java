package edu.example.wayfarer.dto.marker;

import lombok.Data;

@Data
public class MarkerUpdateDTO {
    private Long markerId;
    private Boolean confirm;

    // 로그인 구현 전 임시 필드입니다
    // 로그인 구현 후에 필드는 삭제하고 사용자 정보를 로그인 정보에서 가져옵니다.
    private String email;
}

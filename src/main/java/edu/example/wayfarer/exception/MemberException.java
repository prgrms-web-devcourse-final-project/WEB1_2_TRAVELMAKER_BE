package edu.example.wayfarer.exception;

import org.springframework.http.HttpStatus;

public enum MemberException {
    MEMBER_NOT_FOUND("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final MemberTaskException memberTaskException;

    MemberException(String message, HttpStatus status) {
        memberTaskException = new MemberTaskException(message, status);
    }

    public MemberTaskException get(){
        return memberTaskException;
    }
}

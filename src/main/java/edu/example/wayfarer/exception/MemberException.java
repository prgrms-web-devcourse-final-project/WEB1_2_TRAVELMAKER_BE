package edu.example.wayfarer.exception;

public enum MemberException {
    NOT_FOUND("존재하지 않는 맴버입니다.", 404);

    private final MemberTaskException memberTaskException;

    MemberException(String message, int code) {
        memberTaskException = new MemberTaskException(message, code);
    }

    public MemberTaskException get() {
        return memberTaskException;
    }
}

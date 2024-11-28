package edu.example.wayfarer.exception;

public enum WebSocketException {

    INVALID_TOKEN("Invalid token", 401),
    INVALID_MESSAGE_FORMAT("Invalid message format", 1002), // 1002: Protocol Error
    UNAUTHORIZED("Unauthorized access", 1001), // 1001: Going Away (or use another appropriate code)
    SERVER_ERROR("Server encountered an unexpected error", 1006), // 1006: Abnormal Closure
    INVALID_ACTION("Invalid action", 1003),
    INVALID_EMAIL("Invalid email", 1003);

    private final String message;
    private final int code;

    WebSocketException(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }
    public int getCode() {
        return code;
    }
}

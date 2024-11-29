package edu.example.wayfarer.exception;

public enum GeocodingException {
    NULL_RESPONSE("응답이 없습니다.", 500),
    INVALID_REQUEST("잘못된 요청입니다.", 400),
    REQUEST_DENIED("요청이 거부되었습니다.", 403),
    OVER_QUERY_LIMIT("쿼리 한도를 초과했습니다.", 429),
    ZERO_RESULTS("결과를 찾을 수 없습니다.", 404),
    UNKNOWN_ERROR("알 수 없는 오류가 발생했습니다.", 500)
    ;

    private final GeocodingTaskException geocodingTaskException;

    GeocodingException(String message, int code) {
        geocodingTaskException = new GeocodingTaskException(message, code);
    }

    public GeocodingTaskException get() {
        return geocodingTaskException;
    }
}

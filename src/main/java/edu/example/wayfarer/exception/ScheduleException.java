package edu.example.wayfarer.exception;

public enum ScheduleException {
    NOT_FOUND("존재하지 않는 스케쥴입니다.", 404);

    private final ScheduleTaskException scheduleTaskException;

    ScheduleException(String message, int code) {
        scheduleTaskException = new ScheduleTaskException(message, code);
    }

    public ScheduleTaskException get() {
        return scheduleTaskException;
    }
}

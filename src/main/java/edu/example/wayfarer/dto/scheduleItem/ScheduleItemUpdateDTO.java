package edu.example.wayfarer.dto.scheduleItem;

import java.sql.Time;

public record ScheduleItemUpdateDTO(
        Long scheduleItemId,
        String name,
        Time time,
        String content
) {}

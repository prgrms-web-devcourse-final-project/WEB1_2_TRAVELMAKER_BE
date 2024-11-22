package edu.example.wayfarer.dto.scheduleItem;

import lombok.Builder;
import lombok.Data;

import java.sql.Time;

@Data
@Builder
public class ScheduleItemUpdateDTO {
    private Long scheduleItemId;
    private String name;
    private String address;
    private Time time;
    private String content;
}

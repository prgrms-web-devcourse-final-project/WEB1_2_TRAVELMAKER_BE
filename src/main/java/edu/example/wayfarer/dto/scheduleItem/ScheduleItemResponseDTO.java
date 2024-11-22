package edu.example.wayfarer.dto.scheduleItem;

import lombok.Builder;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDateTime;

@Data
@Builder
public class ScheduleItemResponseDTO {
    private Long scheduleItemId;
    private Long markerId;
    private String name;
    private String address;
    private Time time;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

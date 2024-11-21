package edu.example.wayfarer.converter;

import edu.example.wayfarer.dto.scheduleItem.ScheduleItemResponseDTO;
import edu.example.wayfarer.entity.ScheduleItem;

public class ScheduleItemConverter {
    public static ScheduleItemResponseDTO toScheduleItemResponseDTO(ScheduleItem scheduleItem) {
        return ScheduleItemResponseDTO.builder()
                .scheduleItemId(scheduleItem.getScheduleItemId())
                .markerId(scheduleItem.getMarker().getMarkerId())
                .name(scheduleItem.getName())
                .address(scheduleItem.getAddress())
                .time(scheduleItem.getTime())
                .content(scheduleItem.getContent())
                .createdAt(scheduleItem.getCreatedAt())
                .updatedAt(scheduleItem.getUpdatedAt())
                .build();
    }
}

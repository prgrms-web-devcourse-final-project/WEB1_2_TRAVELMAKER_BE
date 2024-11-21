package edu.example.wayfarer.converter;

import edu.example.wayfarer.dto.marker.MarkerListDTO;
import edu.example.wayfarer.dto.marker.MarkerRequestDTO;
import edu.example.wayfarer.dto.marker.MarkerResponseDTO;
import edu.example.wayfarer.entity.Marker;
import edu.example.wayfarer.entity.Member;
import edu.example.wayfarer.entity.Schedule;

import java.util.List;

public class MarkerConverter {

    public static Marker toMarker(
            MarkerRequestDTO markerRequestDTO,
            Member member,
            Schedule schedule,
            String color
    ) {
        return Marker.builder()
                .member(member)
                .schedule(schedule)
                .lat(markerRequestDTO.getLat())
                .lng(markerRequestDTO.getLng())
                .color(color)
                .confirm(false)
                .build();
    }

    public static MarkerResponseDTO toMarkerResponseDTO(Marker marker) {
        return MarkerResponseDTO.builder()
                .markerId(marker.getMarkerId())
                .email(marker.getMember().getEmail())
                .scheduleId(marker.getSchedule().getScheduleId())
                .lat(marker.getLat())
                .lng(marker.getLng())
                .color(marker.getColor())
                .confirm(marker.getConfirm())
                .createdAt(marker.getCreatedAt())
                .updatedAt(marker.getUpdatedAt())
                .build();
    }

    public static MarkerListDTO toMarkerListDTO(
            Long scheduleId,
            List<MarkerResponseDTO> markerList
    ) {
        return MarkerListDTO.builder()
                .scheduleId(scheduleId)
                .markerList(markerList)
                .build();
    }
}

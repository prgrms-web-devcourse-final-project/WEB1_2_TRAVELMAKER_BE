package edu.example.wayfarer.dto;

import edu.example.wayfarer.entity.Schedule;
import lombok.Builder;

import java.util.List;

@Builder
public class MarkerListDTO {
    private Long scheduleId;
    private List<MarkerResponseDTO> markerList;
}

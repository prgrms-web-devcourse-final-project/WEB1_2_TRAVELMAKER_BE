package edu.example.wayfarer.dto.marker;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
public class MarkerListDTO {
    private Long scheduleId;
    private List<MarkerResponseDTO> markerList;
}

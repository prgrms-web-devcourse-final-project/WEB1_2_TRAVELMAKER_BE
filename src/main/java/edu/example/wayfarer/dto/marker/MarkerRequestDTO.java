package edu.example.wayfarer.dto.marker;

import lombok.Data;

@Data
public class MarkerRequestDTO {
    private String email;
    private Long scheduleId;
    private double lat;
    private double lng;
}

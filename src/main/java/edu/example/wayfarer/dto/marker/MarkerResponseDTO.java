package edu.example.wayfarer.dto.marker;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MarkerResponseDTO {
    private Long markerId;
    private String email;
    private Long scheduleId;
    private double lat;
    private double lng;
    private String color;
    private Boolean confirm;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

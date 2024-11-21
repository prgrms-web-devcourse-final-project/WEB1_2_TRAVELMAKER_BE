package edu.example.wayfarer.dto;

import lombok.Builder;

import java.time.LocalDateTime;

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

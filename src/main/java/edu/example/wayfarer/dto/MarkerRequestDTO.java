package edu.example.wayfarer.dto;

import lombok.Data;

@Data
public class MarkerRequestDTO {
    private String email;
    private Long scheduleId;
    private double lat;
    private double lng;


}

package edu.example.wayfarer.dto.marker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarkerRequestDTO {
    private String email;
    private Long scheduleId;
    private double lat;
    private double lng;
}

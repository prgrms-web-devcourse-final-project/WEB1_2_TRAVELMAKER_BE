package edu.example.wayfarer.service;

import edu.example.wayfarer.dto.marker.MarkerListDTO;
import edu.example.wayfarer.dto.marker.MarkerRequestDTO;
import edu.example.wayfarer.dto.marker.MarkerResponseDTO;
import edu.example.wayfarer.dto.marker.MarkerUpdateDTO;

import java.util.List;

public interface MarkerService {

    MarkerResponseDTO createMarker(MarkerRequestDTO markerRequestDTO);

    MarkerResponseDTO readMarker(Long markerId);

    List<MarkerResponseDTO> readMarkers(Long scheduleId);

    List<MarkerListDTO> readAllMarkers(String roomId);

    MarkerResponseDTO updateMarker(MarkerUpdateDTO markerUpdateDTO);

    void deleteMarker(Long markerId);



}

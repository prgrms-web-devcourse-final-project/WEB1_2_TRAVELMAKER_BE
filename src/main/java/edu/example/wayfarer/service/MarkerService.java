package edu.example.wayfarer.service;

import edu.example.wayfarer.dto.MarkerListDTO;
import edu.example.wayfarer.dto.MarkerRequestDTO;
import edu.example.wayfarer.dto.MarkerResponseDTO;

import java.util.List;

public interface MarkerService {

    MarkerResponseDTO createMarker(MarkerRequestDTO markerRequestDTO);

    MarkerResponseDTO readMarker(Long markerId);

    List<MarkerResponseDTO> readMarkers(Long scheduleId);

    List<MarkerListDTO> readAllMarkers(String roomId);

    MarkerResponseDTO updateMarker(MarkerRequestDTO markerRequestDTO);

    void deleteMarker(Long markerId);



}

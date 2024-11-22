package edu.example.wayfarer.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class ConnectResponseDTO {
    private final String action;
    private final String sessionId;
}

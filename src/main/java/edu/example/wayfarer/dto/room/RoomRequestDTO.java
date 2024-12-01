package edu.example.wayfarer.dto.room;

import java.time.LocalDate;

public record RoomRequestDTO (
        String title,
        String country,
        LocalDate startDate,
        LocalDate endDate,
        String email
){}



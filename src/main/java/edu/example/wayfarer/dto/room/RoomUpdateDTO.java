package edu.example.wayfarer.dto.room;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoomUpdateDTO {
    private String roomId;
    private String title;
    private String country;
}

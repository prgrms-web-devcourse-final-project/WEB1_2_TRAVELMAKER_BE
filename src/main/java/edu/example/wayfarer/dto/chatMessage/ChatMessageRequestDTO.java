package edu.example.wayfarer.dto.chatMessage;

import edu.example.wayfarer.entity.MemberRoom;

public record ChatMessageRequestDTO(
        String roomId,
        String email,
        String content
) {
}

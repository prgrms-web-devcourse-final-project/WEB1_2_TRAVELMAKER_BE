package edu.example.wayfarer.repository;

import edu.example.wayfarer.dto.chatMessage.ChatMessageResponseDTO;
import edu.example.wayfarer.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("SELECT new edu.example.wayfarer.dto.chatMessage.ChatMessageResponseDTO(cm.room.roomId, cm.member.email, cm.content, cm.createdAt, cm.updatedAt) " +
            "FROM ChatMessage cm " +
            "WHERE cm.room.roomId = :roomId")
    List<ChatMessageResponseDTO> findChatMessageResponseDTOByRoomId(String roomId);

    @Modifying
    @Query("DELETE FROM ChatMessage c WHERE c.room.roomId = :roomId")
    void deleteByRoomId(@Param("roomId") String roomId);

    boolean existsByRoom_RoomIdAndMemberEmailAndCreatedAt(String roomId, String sender, LocalDateTime createdAt);
}

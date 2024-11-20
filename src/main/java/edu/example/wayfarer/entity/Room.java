package edu.example.wayfarer.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Room {

    @Id
    private String roomId;  // 랜덤 문자열

    private String title;
    private String country;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String roomCode;
    private String hostEmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "room")
    private List<MemberRoom> memberRooms;



}

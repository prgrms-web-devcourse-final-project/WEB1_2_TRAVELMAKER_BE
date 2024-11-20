package edu.example.wayfarer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Marker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long markerId;
    private String email;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    private Double lat;
    private Double lng;
    private String color;
    private Boolean confirm;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

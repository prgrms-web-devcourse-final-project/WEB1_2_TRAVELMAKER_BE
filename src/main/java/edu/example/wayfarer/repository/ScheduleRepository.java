package edu.example.wayfarer.repository;

import edu.example.wayfarer.entity.Room;
import edu.example.wayfarer.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByRoom_RoomId(String roomId);
}

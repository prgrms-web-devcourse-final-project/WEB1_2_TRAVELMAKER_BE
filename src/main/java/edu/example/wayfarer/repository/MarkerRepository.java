package edu.example.wayfarer.repository;

import edu.example.wayfarer.entity.Marker;
import edu.example.wayfarer.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarkerRepository extends JpaRepository<Marker, Long> {

    List<Marker> findBySchedule_ScheduleId(Long scheduleId);

    Boolean existsBySchedule_ScheduleId(Long scheduleId);
}

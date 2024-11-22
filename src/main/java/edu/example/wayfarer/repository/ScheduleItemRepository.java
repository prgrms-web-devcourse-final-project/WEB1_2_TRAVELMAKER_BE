package edu.example.wayfarer.repository;

import edu.example.wayfarer.entity.ScheduleItem;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleItemRepository extends JpaRepository<ScheduleItem, Long> {

    List<ScheduleItem> findByMarker_Schedule_ScheduleId(Long scheduleId);

    Boolean existsByMarker_MarkerId(Long markerId);

    void deleteByMarker_MarkerId(Long markerId);
}


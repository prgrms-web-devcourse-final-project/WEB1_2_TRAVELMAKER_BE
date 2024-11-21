package edu.example.wayfarer.repository;

import edu.example.wayfarer.entity.ScheduleItem;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleItemRepository extends JpaRepository<ScheduleItem, Long> {

    void deleteByMarker_MarkerId(Long markerId);
}


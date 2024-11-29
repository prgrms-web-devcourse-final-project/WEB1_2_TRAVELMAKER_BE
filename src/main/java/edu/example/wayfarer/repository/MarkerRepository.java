package edu.example.wayfarer.repository;

import edu.example.wayfarer.entity.Marker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarkerRepository extends JpaRepository<Marker, Long> {

    List<Marker> findByScheduleScheduleId(Long scheduleId);

    Optional<Marker> findByScheduleItemScheduleItemId(Long scheduleItemId);

    Boolean existsByScheduleScheduleId(Long scheduleId);

    Long countByScheduleScheduleId(Long scheduleId);

    Long countByScheduleScheduleIdAndConfirmTrue(Long scheduleId);

}

package edu.example.wayfarer.repository;

import edu.example.wayfarer.entity.ScheduleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScheduleItemRepository extends JpaRepository<ScheduleItem, Long> {

    List<ScheduleItem> findByMarker_Schedule_ScheduleId(Long scheduleId);

    Optional<ScheduleItem> findByMarker_MarkerId(Long markerId);

    Boolean existsByMarker_MarkerId(Long markerId);

    void deleteByMarker_MarkerId(Long markerId);

    // 최대 itemOrder 값 조회, null 일 경우 0 반환
    @Query("SELECT COALESCE(MAX(si.itemOrder), 0) " +
            "FROM ScheduleItem si " +
            "WHERE si.marker.schedule.scheduleId = :scheduleId")
    Double findMaxItemOrderByScheduleId(@Param("scheduleId") Long scheduleId);

    // 최소 itemOrder 값 조회, null 일 경우 0 반환
    @Query("SELECT COALESCE(MIN(si.itemOrder), 0) " +
            "FROM ScheduleItem si " +
            "WHERE si.marker.schedule.scheduleId = :scheduleId")
    Double findMinItemOrderByScheduleId(@Param("scheduleId") Long scheduleId);

    List<ScheduleItem> findByMarker_Schedule_ScheduleIdAndItemOrderBetween(Long scheduleId, Double start, Double end);
}


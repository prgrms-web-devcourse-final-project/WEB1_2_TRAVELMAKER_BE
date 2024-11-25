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

    // 최대 index 값 조회, null 일 경우 0 반환
    @Query("SELECT COALESCE(MAX(si.index), 0) " +
            "FROM ScheduleItem si " +
            "WHERE si.marker.schedule.scheduleId = :scheduleId")
    Double findMaxIndexByScheduleId(@Param("scheduleId") Long scheduleId);

    // 최소 index 값 조회, null 일 경우 0 반환
    @Query("SELECT COALESCE(MIN(si.index), 0) " +
            "FROM ScheduleItem si " +
            "WHERE si.marker.schedule.scheduleId = :scheduleId")
    Double findMinIndexByScheduleId(@Param("scheduleId") Long scheduleId);

    List<ScheduleItem> findByMarker_Schedule_ScheduleIdAndIndexBetween(Long scheduleId, Double start, Double end);
}


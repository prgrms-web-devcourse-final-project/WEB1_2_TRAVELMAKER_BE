package edu.example.wayfarer.manager;

import edu.example.wayfarer.dto.common.PageRequestDTO;
import edu.example.wayfarer.dto.scheduleItem.ScheduleItemResponseDTO;
import edu.example.wayfarer.entity.ScheduleItem;

import java.util.List;

public interface ScheduleItemOrderManager {

    int getIndex(ScheduleItem scheduleItem);
    List<ScheduleItemResponseDTO> orderByLinkedList(Long scheduleId);
    List<ScheduleItemResponseDTO> paginate(Long scheduleId, PageRequestDTO pageRequestDTO);
    void updateLinks(ScheduleItem scheduleItem, Long previousItemId, Long nextItemId);
    void removeLinkedList(ScheduleItem scheduleItem);
}

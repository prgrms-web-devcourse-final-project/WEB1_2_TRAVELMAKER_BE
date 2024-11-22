package edu.example.wayfarer.controller;

import edu.example.wayfarer.dto.scheduleItem.ScheduleItemResponseDTO;
import edu.example.wayfarer.dto.scheduleItem.ScheduleItemUpdateDTO;
import edu.example.wayfarer.service.ScheduleItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.sql.Time;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ScheduleItemController {
    private final SimpMessagingTemplate template;
    private final ScheduleItemService scheduleItemService;

    @MessageMapping("room/{roomId}/schedule")
    public void handleSchedule(
            @DestinationVariable String roomId,
            @Payload Map<String, Object> schedulePayload
    ) {
        String action = (String) schedulePayload.get("action");

        switch (action) {
            case "LIST_SCHEDULE":

            case "UPDATE_SCHEDULE":
                Long scheduleItemId = (Long) ((Map<String, Object>) schedulePayload.get("data")).get("scheduleItemId");
                String name = ((Map<String, Object>) schedulePayload.get("data")).get("name").toString();
                String address = ((Map<String, Object>) schedulePayload.get("data")).get("address").toString();
                Time time = (Time) ((Map<String, Object>) schedulePayload.get("data")).get("time");
                String contet = ((Map<String, Object>) schedulePayload.get("data")).get("contet").toString();

                ScheduleItemUpdateDTO scheduleItemUpdateDTO = ScheduleItemUpdateDTO.builder()
                        .scheduleItemId(scheduleItemId)
                        .name(name)
                        .address(address)
                        .time(time)
                        .content(contet)
                        .build();

                ScheduleItemResponseDTO updatedScheduleItem = scheduleItemService.update(scheduleItemUpdateDTO);
                Map<String, Object> updatedScheduleItemMessage = Map.of(
                        "action", "UPDATED_SCHEDULE",
                        "data", Map.of(
                                "scheduleItemId", updatedScheduleItem.getScheduleItemId(),
                                "name", updatedScheduleItem.getName(),
                                "address", updatedScheduleItem.getAddress(),
                                "time", updatedScheduleItem.getTime(),
                                "contet", updatedScheduleItem.getContent()
                        )
                );
                template.convertAndSend("/topic/schedule/" + roomId + "/schedule", updatedScheduleItemMessage);

            case "DELETE_SCHEDULE":

            default:
                throw new IllegalArgumentException("Invalid action: " + action);        }
    }

}

package edu.example.wayfarer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.example.wayfarer.converter.WebSocketMessageConverter;
import edu.example.wayfarer.dto.scheduleItem.ScheduleItemResponseDTO;
import edu.example.wayfarer.dto.scheduleItem.ScheduleItemUpdateDTO;
import edu.example.wayfarer.service.ScheduleItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.sql.Time;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ScheduleItemController {
    private final SimpMessagingTemplate template;
    private final ScheduleItemService scheduleItemService;
    private final ObjectMapper objectMapper;

    @MessageMapping("room/{roomId}/schedule")
    public void handleSchedule(
            @DestinationVariable String roomId,
            @Payload Map<String, Object> schedulePayload
    ) {
        String action = (String) schedulePayload.get("action");

        switch (action) {
            case "LIST_SCHEDULE":
                Long scheduleId = (Long) ((Map<String, Object>) schedulePayload.get("data")).get("scheduleId");
                List<ScheduleItemResponseDTO> scheduleItems = scheduleItemService.getListBySchedule(scheduleId);

                WebSocketMessageConverter<List<ScheduleItemResponseDTO>> listConverter = new WebSocketMessageConverter<>(objectMapper);

                WebSocketMessageConverter.WebsocketMessage<List<ScheduleItemResponseDTO>> listSchedulesMessage=
                listConverter.createMessage("LIST_SCHEDULES", scheduleItems);

                template.convertAndSend("/topic/schedule/" + roomId + "/schedule", listSchedulesMessage);


            case "UPDATE_SCHEDULE":
                //schedulePayload로 받아온 값으로 ScheduleItemUpdateDTO 생성
                Long scheduleItemId = (Long) ((Map<String, Object>) schedulePayload.get("data")).get("scheduleItemId");
                String name = ((Map<String, Object>) schedulePayload.get("data")).get("name").toString();
                //String address = ((Map<String, Object>) schedulePayload.get("data")).get("address").toString();
                Time time = (Time) ((Map<String, Object>) schedulePayload.get("data")).get("time");
                String contet = ((Map<String, Object>) schedulePayload.get("data")).get("contet").toString();

                ScheduleItemUpdateDTO scheduleItemUpdateDTO = ScheduleItemUpdateDTO.builder()
                        .scheduleItemId(scheduleItemId)
                        .name(name)
                        //.address(address)
                        .time(time)
                        .content(contet)
                        .build();

                // ScheduleItemUpdateDTO로 scheduleItemService.update() 실행
                ScheduleItemResponseDTO updatedScheduleItem = scheduleItemService.update(scheduleItemUpdateDTO);

                // WebSocketMessageConverter를 사용해 메시지 객체 생성
                WebSocketMessageConverter<ScheduleItemResponseDTO> updateConverter = new WebSocketMessageConverter<>();
                WebSocketMessageConverter.WebsocketMessage<ScheduleItemResponseDTO> updatedScheduleItemMessage =
                        updateConverter.createMessage("UPDATED_SCHEDULE", updatedScheduleItem);

                //생성한 메시지를 "topic/schedule/{roomId}/schedule" 을 구독한 클라이언트들에게 브로드캐스팅합니다.
                template.convertAndSend("/topic/schedule/" + roomId + "/schedule", updatedScheduleItemMessage);

            case "DELETE_SCHEDULE":
                //1. 스케쥴 아이템 삭제
                //2. 마커 컨펌: true->false, 마커 color: red-> 사용자색으로 변경
                //3. 스케줄 아이템 삭제 메시지 전송, 마커 업데이트 메시지 전송

            default:
                throw new IllegalArgumentException("Invalid action: " + action);
        }
    }

    public String convertListToJsonMessage(List<ScheduleItemResponseDTO> scheduleItems) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(scheduleItems);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert list to JSON", e);
        }
    }

}

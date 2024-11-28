package edu.example.wayfarer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.example.wayfarer.converter.WebSocketMessageConverter;
import edu.example.wayfarer.dto.scheduleItem.ScheduleItemResponseDTO;
import edu.example.wayfarer.dto.scheduleItem.ScheduleItemUpdateDTO;
import edu.example.wayfarer.exception.WebSocketException;
import edu.example.wayfarer.exception.WebSocketTaskException;
import edu.example.wayfarer.service.ScheduleItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
            @Payload Map<String, Object> schedulePayload,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        // WebSocket 세션에서 email 값을 가져오기
        String email = (String) headerAccessor.getSessionAttributes().get("email");
        // email이 null일 경우 예외 처리
        if (email == null) {
            throw new WebSocketTaskException(WebSocketException.INVALID_EMAIL);
        }

        String action = (String) schedulePayload.get("action");
        Map<String, Object> data = (Map<String, Object>) schedulePayload.get("data");

        switch (action) {
            case "LIST_SCHEDULES":
                Long scheduleId = ((Number) data.get("scheduleId")).longValue();
                List<ScheduleItemResponseDTO> scheduleItems = scheduleItemService.getListBySchedule(scheduleId);

                WebSocketMessageConverter<List<ScheduleItemResponseDTO>> listConverter = new WebSocketMessageConverter<>();

                WebSocketMessageConverter.WebsocketMessage<List<ScheduleItemResponseDTO>> listSchedulesMessage=
                listConverter.createMessage("LIST_SCHEDULES", scheduleItems);

                template.convertAndSend("/topic/room/" + roomId + "/schedule", listSchedulesMessage);

                break;


            case "UPDATE_SCHEDULE":
                //schedulePayload로 받아온 값으로 ScheduleItemUpdateDTO 생성
                Long scheduleItemId = ((Number) data.get("scheduleItemId")).longValue();
                String name = data.get("name").toString();
                String content = data.get("content").toString();
                Long previousItemId = Optional.ofNullable((Number) data.get("previousItemId"))
                        .map(Number::longValue)
                        .orElse(null);

                Long nextItemId = Optional.ofNullable((Number) data.get("nextItemId"))
                        .map(Number::longValue)
                        .orElse(null);



                ScheduleItemUpdateDTO scheduleItemUpdateDTO = new ScheduleItemUpdateDTO(scheduleItemId,name,content,previousItemId,nextItemId);

                // ScheduleItemUpdateDTO로 scheduleItemService.update() 실행
                ScheduleItemResponseDTO updatedScheduleItem = scheduleItemService.update(scheduleItemUpdateDTO);

                // WebSocketMessageConverter를 사용해 메시지 객체 생성
                WebSocketMessageConverter<ScheduleItemResponseDTO> updateConverter = new WebSocketMessageConverter<>();
                WebSocketMessageConverter.WebsocketMessage<ScheduleItemResponseDTO> updatedScheduleItemMessage =
                        updateConverter.createMessage("UPDATED_SCHEDULE", updatedScheduleItem);

                //생성한 메시지를 "topic/schedule/{roomId}/schedule" 을 구독한 클라이언트들에게 브로드캐스팅합니다.
                template.convertAndSend("/topic/room/" + roomId + "/schedule", updatedScheduleItemMessage);

                break;

            case "DELETE_SCHEDULE":
                //1. 스케쥴 아이템 삭제
                Long deleteScheduleItemId = ((Number) data.get("scheduleItemId")).longValue();
                scheduleItemService.delete(deleteScheduleItemId);

                //2. 스케줄 아이템 삭제 메시지 전송, 마커 업데이트 메시지 전송
                Map<String, Object> deletedScheduleItemMessage = new LinkedHashMap<>();
                deletedScheduleItemMessage.put("action", "DELETED_SCHEDULE");
                deletedScheduleItemMessage.put("data", Map.of(
                        "message", "일정이 삭제되었습니다."
                ));

                //3. 생성한 메시지를 "topic/schedule/{roomId}/schedule" 을 구독한 클라이언트들에게 브로드캐스팅합니다.
                template.convertAndSend("/topic/room/" + roomId + "/schedule", deletedScheduleItemMessage);

                break;


            default:
                throw new WebSocketTaskException(WebSocketException.INVALID_ACTION);
        }
    }

}

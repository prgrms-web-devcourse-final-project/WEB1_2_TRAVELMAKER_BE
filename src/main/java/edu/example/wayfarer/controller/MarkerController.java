package edu.example.wayfarer.controller;

import edu.example.wayfarer.dto.marker.MarkerRequestDTO;
import edu.example.wayfarer.dto.marker.MarkerResponseDTO;
import edu.example.wayfarer.dto.marker.MarkerUpdateDTO;
import edu.example.wayfarer.dto.scheduleItem.ScheduleItemResponseDTO;
import edu.example.wayfarer.repository.ScheduleItemRepository;
import edu.example.wayfarer.service.MarkerService;
import edu.example.wayfarer.service.ScheduleItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MarkerController {
    private final SimpMessagingTemplate template;
    private final MarkerService markerService;
    private final ScheduleItemService scheduleItemService;

    @MessageMapping("room/{roomId}/map")
    public void handleMarker(
            @DestinationVariable String roomId,
            @Payload Map<String, Object> markerPayload,
            StompHeaderAccessor headerAccessor
    ) {

        String action = (String) markerPayload.get("action");
        /*
        클라이언트에서 송신한 메시지의 "action" 항목으로 기능 구분
        MarkerService의 메서드 리턴값인 MarkerResponseDTO의 내용으로
        클라이언트에 브로드캐스팅 할 JSON 메시지 구현
        */

        switch (action) {
            case "ADD_MARKER":

                //클라이언트가 송신한 메시지인 markerPayload에서 scheduleId, lat, lng 값을 추출
                Long scheduleId = ((Number) ((Map<String, Object>) markerPayload.get("data")).get("scheduleId")).longValue();
                Double lat = ((Number) ((Map<String, Object>) markerPayload.get("data")).get("lat")).doubleValue();
                Double lng = ((Number) ((Map<String, Object>) markerPayload.get("data")).get("lng")).doubleValue();

                //세션에 저장해놓은 email 값을 추출
                Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
                if (sessionAttributes == null || !sessionAttributes.containsKey("email")) {
                    throw new IllegalArgumentException("Email not found in session");
                }
                String email = (String) sessionAttributes.get("email");

                //추출 한 값으로 MarkerRequestDTO 생성
                MarkerRequestDTO markerRequestDTO = MarkerRequestDTO.builder()
                        .email(email)
                        .scheduleId(scheduleId)
                        .lat(lat)
                        .lng(lng)
                        .build();

                MarkerResponseDTO addedMarker = markerService.create(markerRequestDTO);
                Map<String, Object> addedMarkerMessage = Map.of(
                        "action", "ADDED_MARKER",
                        "data", Map.of(
                                "markerId", addedMarker.getMarkerId(),
                                "email", addedMarker.getEmail(),
                                "scheduleId", addedMarker.getScheduleId(),
                                "lat", addedMarker.getLat(),
                                "lng", addedMarker.getLng(),
                                "color", addedMarker.getColor(),
                                "confirm", addedMarker.getConfirm()
                        )
                );

                template.convertAndSend("/topic/room/" + roomId + "/map", addedMarkerMessage);

            case "UPDATE_MARKER":

                //클라이언트가 송신한 메시지인 markerPayload에서 markerId, confirm 값을 추출
                Long markerId = (Long) ((Map<String, Object>) markerPayload.get("data")).get("markerId");
                Boolean confirm = (Boolean) ((Map<String, Object>) markerPayload.get("data")).get("confirm");

                //추출 한 값으로 MarkerUpdateDTO 생성
                MarkerUpdateDTO markerUpdateDTO = MarkerUpdateDTO.builder()
                        .markerId(markerId)
                        .confirm(confirm)
                        .build();

                MarkerResponseDTO updatedMarker = markerService.update(markerUpdateDTO);
                Map<String, Object> updatedMarkerMessage = Map.of(
                        "action", "UPDATED_MARKER",
                        "data", Map.of(
                                "message", "마커가 수정되었습니다.",
                                "markerId", updatedMarker.getMarkerId(),
                                "confirm", updatedMarker.getConfirm()
                        )
                );

                if(confirm == true) {
                    ScheduleItemResponseDTO foundScheduleItem =  scheduleItemService.readByMarkerId(markerId);
                    Map<String, Object> createdScheduleItemMessage = Map.of(
                            "action", "ADDED_SCHEDULE",
                            "data", Map.of(
                                    "scheduleItemId", foundScheduleItem.getScheduleItemId(),
                                    "markerId", foundScheduleItem.getMarkerId(),
                                    "name", foundScheduleItem.getName(),
                                    "address", foundScheduleItem.getAddress(),
                                    "time", foundScheduleItem.getTime(),
                                    "content", foundScheduleItem.getContent()
                            )
                    );
                    template.convertAndSend("/topic/room/" + roomId + "/schedule", createdScheduleItemMessage);
                }

                template.convertAndSend("/topic/room/" + roomId + "/map", updatedMarkerMessage);


            case "DELETE_MARKER":
                //클라이언트가 송신한 메시지인 markerPayload에서 markerId 값을 추출
                Long deleteMarkerId = (Long) ((Map<String, Object>) markerPayload.get("data")).get("markerId");
                //추출한 markerId를 매개변수로 delete 메서드 실행
                markerService.delete(deleteMarkerId);
                Map<String, Object> deletedMarkerMessage = Map.of(
                        "action", "DELETED_MARKER",
                        "data", Map.of(
                                "message", "마커가 삭제되었습니다."
                        )
                );
                template.convertAndSend("/topic/room/" + roomId + "/map", deletedMarkerMessage);

            default:
                throw new IllegalArgumentException("Invalid action: " + action);
        }


    }
}

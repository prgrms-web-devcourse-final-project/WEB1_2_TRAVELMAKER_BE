package edu.example.wayfarer.controller;

import edu.example.wayfarer.exception.WebSocketException;
import edu.example.wayfarer.exception.WebSocketTaskException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ChatController {
    private final SimpMessagingTemplate template;

    @MessageMapping("/room/{roomId}")
    public void handleChatMessage(
            @DestinationVariable String roomId,
            @Payload Map<String, Object> messagePayload,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        // WebSocket 세션에서 email 값을 가져오기
        String email = (String) headerAccessor.getSessionAttributes().get("email");
        // email이 null일 경우 예외 처리
        if (email == null) {
            throw new WebSocketTaskException(WebSocketException.INVALID_EMAIL);
        }
        String action = (String) messagePayload.get("action");
        log.debug("action: {}", action);

        // 클라이언트가 ENTER_ROOM 액션으로 메시지를 보내면 입장 메시지를 보냅니다.
        if("ENTER_ROOM".equals(action)) {
            Map<String, Object> welcomeMessage = new LinkedHashMap<>();
            welcomeMessage.put("action", "WELCOME_MESSAGE");
            welcomeMessage.put("data", Map.of(
                    "sender", "System",
                    "message", email + " 님이 입장하셨습니다.",
                    "timestamp", new Date().toString()
            ));

            log.info("WELCOME MESSAGE: " + welcomeMessage);

            template.convertAndSend("/topic/room/" + roomId, welcomeMessage);

            // 클라이언트가 SEND_MESSAGE 액션으로 메시지를 보내면 BROADCAST_MESSAGE 를 보냅니다.
        } else if ("SEND_MESSAGE".equals(action)) {
            String message = (String) ((Map<String, Object>) messagePayload.get("data")).get("message");
            if (message == null) {
                throw new WebSocketTaskException(WebSocketException.INVALID_MESSAGE_FORMAT);
            }

            Map<String, Object> broadcastMessage = new LinkedHashMap<>();
            broadcastMessage.put("action", "BROADCAST_MESSAGE");
            broadcastMessage.put("data", Map.of(
                    "sender", email,
                    "message", message,
                    "timestamp", new Date().toString()
            ));
            log.info("BROADCAST MESSAGE: " + broadcastMessage);
            template.convertAndSend("/topic/room/" + roomId, broadcastMessage);
        } else {
            throw new WebSocketTaskException(WebSocketException.INVALID_ACTION);
        }
    }
}
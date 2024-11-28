package edu.example.wayfarer.controller;

import edu.example.wayfarer.exception.WebSocketException;
import edu.example.wayfarer.exception.WebSocketTaskException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Log4j2
public class WebSocketConnectController {
    private final SimpMessagingTemplate template;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {

        // StompHeaderAccessor를 사용하여 헤더 정보와 세션 속성에 접근
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // 세션에서 email 속성 추출
        String email = (String) headerAccessor.getSessionAttributes().get("email");

        Map<String, Object> connectedMessage = new LinkedHashMap<>();
        connectedMessage.put("action", "CONNECTED");
        connectedMessage.put("data", Map.of(
                "email", email,
                "message", "연결이 완료되었습니다."
        ));

        template.convertAndSendToUser(email,"/queue/connect", connectedMessage);
        log.info("WebSocket Connected Email: " + email);
    }
}
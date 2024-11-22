package edu.example.wayfarer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class WebSocketConnectController {
    private final SimpMessagingTemplate template;

    @EventListener
    public void handleWebSocketConnectLitener(SessionConnectEvent event) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Map<String, Object> headers = (Map<String, Object>) headerAccessor.getHeader("simpConnectMessage");

        String email = null;

//        if (headers != null && headers.get("Authorization") != null) {
//            String token = (String) headers.get("Authorization");
                //이 단계에서 token에는 bearer가 포함되어있다.
                //bearer 빼고 {JWT}만 담기게끔해야함
//        if (authToken != null && authToken.startsWith("Bearer ")) {
//            authToken = authToken.substring(7).trim();
//        }
//            try {
//                // JWT에서 이메일 추출
//                email = extractEmailFromJwt(token);
//            } catch (Exception e) {
//                log.warn("JWT 파싱 실패: " + e.getMessage());
//            }
//        }
        // 세션에 email 저장
//        headerAccessor.getSessionAttributes().put("email", email);

        Map<String, Object> connectedMessage = Map.of(
                "action", "CONNECTED",
                "data", Map.of(
                        "email", email,
                        "message", "연결이 완료되었습니다."
                )
        );

        template.convertAndSendToUser(email,"/topic/connect", connectedMessage);
        log.info("WebSocket Connected Email: " + email);
    }

}
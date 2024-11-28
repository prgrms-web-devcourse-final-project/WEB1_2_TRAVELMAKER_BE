package edu.example.wayfarer.config;

import edu.example.wayfarer.exception.WebSocketException;
import edu.example.wayfarer.exception.WebSocketTaskException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class WebSocketHandSakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        try{
            //요청 헤더에서 토큰 추출
            String token = request.getHeaders().getFirst("Authorization");
            if (token == null) {
                throw new WebSocketTaskException(WebSocketException.INVALID_TOKEN);
            }
            // "Bearer " 부분 제거
            String jwtToken = token.substring(7).trim();
            //JWT에서 이메일 추출
            String email = extractEmailFomeJwt(jwtToken);

            //동일한 이메일 존재하는지 검증과정 필요

            //세션에 이메일 추가
            attributes.put("email", email);

            return true;
        } catch (WebSocketTaskException e) {
            response.setStatusCode(HttpStatusCode.valueOf(e.getCode()));
            return false;
        }
    }

    private String extractEmailFomeJwt(String jwtToken) {
        // JWT 토큰을 디코딩하고 이메일 정보를 추출하는 로직을 구현해야 함
        // 예: JWT 파싱 라이브러리를 사용하여 이메일 추출
        // 여기서는 가상의 예시로 반환
        return "member1@abc.com"; // 실제 이메일 추출 로직 필요
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

}

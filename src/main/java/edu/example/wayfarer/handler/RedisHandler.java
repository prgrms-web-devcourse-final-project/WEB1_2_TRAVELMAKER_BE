package edu.example.wayfarer.handler;

import edu.example.wayfarer.dto.chatMessage.ChatMessageRequestDTO;
import edu.example.wayfarer.service.ChatMessageService;
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static edu.example.wayfarer.handler.ChatHandler.CHAT_CACHE_PREFIX;

@Component
@RequiredArgsConstructor
@Log4j2
public class RedisHandler {

    @Qualifier("jsonRedisTemplate")
    private final RedisTemplate<String, Object> jsonRedisTemplate;
    private final ChatMessageService chatMessageService;

    @Scheduled(fixedRate = 60000)
    public void migrateMessagesTODB() {
        log.info("Starting Redis to DB migration...");

        Set<String> keys = jsonRedisTemplate.keys(CHAT_CACHE_PREFIX+"*");
        log.info("Found keys: {}", keys);
        if (keys == null || keys.isEmpty()) {
            log.info("No keys found in Redis.");
            log.info("Migration complete.");
            return;
        }

        for (String key : keys) {
            List<Object> messageList = jsonRedisTemplate.opsForList().range(key, 0, -1);
            log.info("messageList: {}", messageList);

            for (Object message : messageList) {
                if (!(message instanceof Map<?,?> messageData)) {
                    log.error("Unexpected message data format in key: {}", key);
                    throw new RedisException("Unexpected message data format in key");
                }

                Map<?, ?> data = (Map<?, ?>) messageData.get("data");

                String roomId = key.split(":")[1];
                String email = (String) data.get("sender");
                String content = (String) data.get("message");
                String timestamp = (String) data.get("timestamp");

                // ChatMessageRequestDTO 객체 생성
                ChatMessageRequestDTO chatMessageRequestDTO = new ChatMessageRequestDTO(roomId, email, content, timestamp);

                // Redis 데이터가 이미 DB에 저장된 메시지인지 확인
                if (!chatMessageService.isMessageExistsInDB(chatMessageRequestDTO)) {
                    // 필터링된 메시지들만 DB에 저장
                    chatMessageService.createChatMessage(chatMessageRequestDTO);
                    log.info("Message saved to DB: roomId={}, email={}, content={}", roomId, email, content);
                } else {
                    log.info("Message already exists in DB: roomId={}, email={}, content={}", roomId, email, content);
                }
            }
            jsonRedisTemplate.delete(key);
            log.info("Redis Key deleted");
        }
        log.info("Migration complete.");
    }
}

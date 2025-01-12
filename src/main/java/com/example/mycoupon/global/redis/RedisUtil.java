package com.example.mycoupon.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, Object> redisBlackListTemplate;

    // refresh 처리 용도
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, Object o, Long min) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(o.getClass())); // 객체를 json으로 직렬화
        redisTemplate.opsForValue().set(key, o, min, TimeUnit.MINUTES);
    }

    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    // black list 처리 용도
    public Object getBlackList(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void setBlackList(String key, Object o, Long min) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(o.getClass())); // 객체를 json으로 직렬화
        redisTemplate.opsForValue().set(key, o, min, TimeUnit.MINUTES);
    }

    public boolean deleteBlackList(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    public boolean hasKeyBlackList(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }


}

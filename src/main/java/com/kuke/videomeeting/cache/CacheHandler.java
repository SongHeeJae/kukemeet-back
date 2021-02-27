package com.kuke.videomeeting.cache;

import com.kuke.videomeeting.config.cache.CacheKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CacheHandler {

    @Cacheable(value = CacheKey.USER, key = "#userId")
    public void createUserCache(Long userId) {
    }
}

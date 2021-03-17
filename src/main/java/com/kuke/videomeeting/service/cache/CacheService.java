package com.kuke.videomeeting.service.cache;

import com.kuke.videomeeting.config.cache.CacheKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheService {

    private final CacheManager cacheManager;

    @Cacheable(value = CacheKey.CODE, key="#uid", unless = "#result == null")
    public String createCodeCache(String uid, String code) {
        log.debug("create code cache - " + uid);
        return code;
    }

    @CacheEvict(value = CacheKey.CODE, key="#uid")
    public void deleteCodeCache(String uid) {
        log.debug("delete code cache - " + uid);
    }

    public String readCodeCache(String uid) {
        return cacheManager.getCache(CacheKey.CODE).get(uid).get().toString();
    }

}

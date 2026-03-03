package com.stocknews.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Cache configuration using Caffeine.
 * Three separate caches with different TTLs:
 * <ul>
 *   <li>{@code news}            – 15 minutes</li>
 *   <li>{@code quotes}          – 5 minutes</li>
 *   <li>{@code recommendations} – 60 minutes</li>
 * </ul>
 */
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager();
        manager.registerCustomCache("news",
                Caffeine.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).maximumSize(200).build());
        manager.registerCustomCache("quotes",
                Caffeine.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).maximumSize(500).build());
        manager.registerCustomCache("recommendations",
                Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES).maximumSize(500).build());
        return manager;
    }
}

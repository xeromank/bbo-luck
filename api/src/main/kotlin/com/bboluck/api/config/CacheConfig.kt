package com.bboluck.api.config

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.gotson.spring.session.caffeine.config.annotation.web.http.EnableCaffeineHttpSession
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

const val LOCAL_CACHE_TTL = 30L

@Configuration
@EnableCaching
@EnableCaffeineHttpSession(maxInactiveIntervalInSeconds = 3600)
class CacheConfig {

    @Bean
    fun caffeineConfig(): Caffeine<Any, Any> {
        return Caffeine
            .newBuilder()
            .expireAfterWrite(LOCAL_CACHE_TTL, TimeUnit.MINUTES)
    }

    @Bean
    fun cacheManager(caffeine: Caffeine<Any, Any>): CacheManager {
        val caffeineCacheManager = CaffeineCacheManager()
        caffeineCacheManager.setCaffeine(caffeine)
        return caffeineCacheManager
    }
}

package com.onboard.server.global.config

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import redis.embedded.RedisServer

@TestConfiguration
class EmbeddedRedisConfig(
    @Value("\${spring.data.redis.port}")
    private val redisPort: Int,
) {
    private val redisServer: RedisServer = RedisServer(redisPort)

    @PostConstruct
    fun startRedis() {
        redisServer.start()
    }

    @PreDestroy
    fun stopRedis() {
        redisServer.stop()
    }
}

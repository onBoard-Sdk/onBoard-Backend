package com.onboard.server.global.config

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import redis.embedded.RedisServer
import java.io.BufferedReader
import java.io.InputStreamReader


@TestConfiguration
class EmbeddedRedisConfig(
    @Value("\${spring.data.redis.port}")
    private val redisPort: Int,
) {
    private lateinit var redisServer: RedisServer

    @PostConstruct
    fun startRedis() {
        val port = if (isRedisRunning()) findAvailablePort() else redisPort
        redisServer = RedisServer(port)
        redisServer.start()
    }

    @PreDestroy
    fun stopRedis() {
        redisServer.stop()
    }

    private fun isRedisRunning(): Boolean {
        return isRunning(executeGrepProcessCommand(redisPort))
    }

    fun findAvailablePort(): Int {
        for (port in 10000..65535) {
            val process = executeGrepProcessCommand(port)
            if (!isRunning(process)) {
                return port
            }
        }

        throw IllegalArgumentException("Not Found Available port: 10000 ~ 65535")
    }

    private fun executeGrepProcessCommand(port: Int): Process {
        val command = String.format("netstat -nat | grep LISTEN|grep %d", port)
        val shell = arrayOf("/bin/sh", "-c", command)
        return Runtime.getRuntime().exec(shell)
    }

    private fun isRunning(process: Process): Boolean {
        var line: String?
        val pidInfo = StringBuilder()

        try {
            BufferedReader(InputStreamReader(process.inputStream)).use { input ->
                while ((input.readLine().also {
                        line = it
                    }) != null) {
                    pidInfo.append(line)
                }
            }
        } catch (e: Exception) {
            throw IllegalArgumentException()
        }


        return pidInfo.toString().isNotEmpty()
    }
}

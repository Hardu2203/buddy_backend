package com.example.buddy_backend

import com.example.buddy_backend.config.BlockchainConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
@EnableConfigurationProperties(BlockchainConfig::class)
class BuddyBackendApplication

fun main(args: Array<String>) {
	runApplication<BuddyBackendApplication>(*args)
}

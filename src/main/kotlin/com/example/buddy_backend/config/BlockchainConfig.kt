package com.example.buddy_backend.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "blockchain")
class BlockchainConfig{
    var publicKey: String = ""
    var privateKey: String = ""
    var nodeAddress: String = ""
}
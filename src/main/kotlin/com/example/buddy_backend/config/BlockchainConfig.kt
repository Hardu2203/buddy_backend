package com.example.buddy_backend.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Uint
import org.web3j.abi.datatypes.Utf8String

@Configuration
@ConfigurationProperties(prefix = "blockchain")
@Primary
class BlockchainConfig{
    var publicKey: String = ""
    var privateKey: String = ""
    var nodeAddress: String = ""
    var contractId: String = ""

    var transferEventParams = listOf<TypeReference<*>>(
        object : TypeReference<Address>(false) {},
        object : TypeReference<Address>(false) {},
        object : TypeReference<Address>(false) {},
        object : TypeReference<Uint>(false) {},
    )

    var depositEventParams = listOf<TypeReference<*>>(
        object : TypeReference<Address>(false) {},
        object : TypeReference<Address>(false) {},
        object : TypeReference<Uint>(false) {},
    )

    var withdrawEventParams = listOf<TypeReference<*>>(
        object : TypeReference<Address>(false) {},
        object : TypeReference<Address>(false) {},
        object : TypeReference<Uint>(false) {},
    )
}
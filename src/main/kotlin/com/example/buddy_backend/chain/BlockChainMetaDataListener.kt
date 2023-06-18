package com.example.buddy_backend.chain

import com.example.buddy_backend.config.BlockchainConfig
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.methods.response.EthBlockNumber
import org.web3j.protocol.http.HttpService

@Service
class BlockChainMetaDataListener(
    private val blockChainConfig: BlockchainConfig,
    private val blockChainRepository: BlockChainRepository
) {

    @Scheduled(initialDelay = 100000, fixedRate = 3600000)
    fun getBlockNumber() {
        val web3j = Web3j.build(HttpService(blockChainConfig.nodeAddress))
        var result: EthBlockNumber? = EthBlockNumber()
        result = web3j.ethBlockNumber()
            .sendAsync()
            .get()

        val blockChain = blockChainRepository.findTopByOrderByIdDesc() ?: BlockChain(blockNumber = result.blockNumber)
        blockChain.blockNumber = result.blockNumber
        blockChainRepository.save(blockChain)
    }

}

private val logger = KotlinLogging.logger {  }
package com.example.buddy_backend.sell_order

import com.example.buddy_backend.chain.BlockChainRepository
import com.example.buddy_backend.config.BlockchainConfig
import io.reactivex.disposables.Disposable
import jakarta.annotation.PostConstruct
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.web3j.abi.EventEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Event
import org.web3j.abi.datatypes.Type
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.protocol.http.HttpService
import java.math.BigInteger


@Service
class SellEventListener(
    private val blockChainConfig: BlockchainConfig,
    private val blockChainRepository: BlockChainRepository,
    private val sellOrderService: SellOrderService,
) {

    private lateinit var web3j: Web3j
    lateinit var ethFilter: EthFilter
    lateinit var payFilter: EthFilter
    var processPaymentSubscriber: Disposable? = null
    var createSubscriber: Disposable? = null


    @PostConstruct
//    @Scheduled(fixedRate = 60000)
    fun initEventListener() {
        try {
            web3j = Web3j.build(HttpService(blockChainConfig.nodeAddress))

            val fromBlock =
                DefaultBlockParameter.valueOf(blockChainRepository.findTopByOrderByIdDesc()?.blockNumber?.min(BigInteger.TWO))
            ethFilter = EthFilter(
                fromBlock,
                DefaultBlockParameterName.LATEST,
                blockChainConfig.contractId
            )

            val processPaymentEvent = Event("Transfer", blockChainConfig.transferEventParams)
            ethFilter.addSingleTopic(EventEncoder.encode(processPaymentEvent))

            registerSubscriber()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun registerSubscriber() {

        processPaymentSubscriber?.dispose()
        processPaymentSubscriber = web3j.ethLogFlowable(ethFilter).subscribe(
            { event ->
                when (event.type) {
                    TRANSFER_EVENT -> {
                        val transferData = FunctionReturnDecoder.decode(
                            event.data,
                            blockChainConfig.transferEventParams as MutableList<TypeReference<Type<Any>>>?
                        )
                        sellOrderService.transferTokens(
                            transferData[0].value as Address,
                            transferData[1].value as Address,
                            transferData[2].value as Address,
                            transferData[3].value as BigInteger,
                        )
                    }
                    DEPOSIT_EVENT -> {
                        val depositData = FunctionReturnDecoder.decode(
                            event.data,
                            blockChainConfig.depositEventParams as MutableList<TypeReference<Type<Any>>>?
                        )
                        sellOrderService.createSellOrder(
                            event.transactionHash,
                            depositData[0].value as Address,
                            depositData[1].value as Address,
                            depositData[3].value as BigInteger,
                        )
                    }
                    WITHDRAW_EVENT -> {
                        val withdrawData = FunctionReturnDecoder.decode(
                            event.data,
                            blockChainConfig.withdrawEventParams as MutableList<TypeReference<Type<Any>>>?
                        )
                        sellOrderService.cancelSellOrder(
                            withdrawData[0].value as Address,
                            withdrawData[1].value as Address,
                            withdrawData[3].value as BigInteger,
                        )
                    }
                }

            },
            { err ->
                println("Blockchain unreachable: ${err.message}")
                Thread.sleep(10000)
                registerSubscriber()
            },
        )
    }

    companion object {

        const val TRANSFER_EVENT = "Transfer"
        const val DEPOSIT_EVENT = "Deposit"
        const val WITHDRAW_EVENT = "Withdraw"

    }

}

private val logger = KotlinLogging.logger { }
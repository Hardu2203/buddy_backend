package com.example.buddy_backend.sell_order

import com.example.buddy_backend.chain.BlockChainRepository
import com.example.buddy_backend.config.BlockchainConfig
import com.example.buddy_backend.config.CurrencyEnum
import io.reactivex.disposables.Disposable
import jakarta.annotation.PostConstruct
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.web3j.abi.EventEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
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
    lateinit var transferFilter: EthFilter
    lateinit var depositFilter: EthFilter
    lateinit var withdrawFilter: EthFilter
    var transferSubscriber: Disposable? = null
    var withdrawSubscriber: Disposable? = null
    var depositSubscriber: Disposable? = null


    @PostConstruct
    @Scheduled(fixedRate = 60000)
    fun initEventListener() {
        try {
            web3j = Web3j.build(HttpService(blockChainConfig.nodeAddress))

            val fromBlock =
                DefaultBlockParameter.valueOf(blockChainRepository.findTopByOrderByIdDesc()?.blockNumber?.minus(BigInteger.TWO))
            transferFilter = EthFilter(
                fromBlock,
//                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST,
                blockChainConfig.contractId
            )

            depositFilter = EthFilter(
                fromBlock,
//                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST,
                blockChainConfig.contractId
            )

            withdrawFilter = EthFilter(
                fromBlock,
//                DefaultBlockParameterName.EARLIEST,
                DefaultBlockParameterName.LATEST,
                blockChainConfig.contractId
            )


            val deposit = Event(DEPOSIT_EVENT, blockChainConfig.depositEventParams)
            depositFilter.addSingleTopic(EventEncoder.encode(deposit))

            val transferEvent = Event(TRANSFER_EVENT, blockChainConfig.transferEventParams)
            transferFilter.addSingleTopic(EventEncoder.encode(transferEvent))

            val withdrawEvent = Event(WITHDRAW_EVENT, blockChainConfig.withdrawEventParams)
            withdrawFilter.addSingleTopic(EventEncoder.encode(withdrawEvent))


            registerSubscriber('D')
            registerSubscriber('T')
            registerSubscriber('W')

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun registerSubscriber(type: Char) {
        when (type) {
            'D' -> depositSubscriber?.dispose()
            'T' -> transferSubscriber?.dispose()
            'W' -> transferSubscriber?.dispose()
        }

        when (type) {
            'D' -> depositSubscriber = web3j.ethLogFlowable(depositFilter).subscribe(
                { event ->
                    val depositData = FunctionReturnDecoder.decode(
                        event.data,
                        blockChainConfig.depositEventParams as MutableList<TypeReference<Type<Any>>>?
                    )
                    sellOrderService.createSellOrder(
                        depositData[0].value as BigInteger,
                        depositData[1].value as String,
                        depositData[2].value as String,
                        depositData[3].value as BigInteger,
                        CurrencyEnum.ZAR,
                        depositData[5].value as BigInteger,
                    )
                },
                { err ->
                    println("Blockchain unreachable: ${err.message}")
                    Thread.sleep(10000)
                    registerSubscriber('D')
                },
            )

            'T' -> transferSubscriber = web3j.ethLogFlowable(transferFilter).subscribe(
                { event ->
                    val transferData = FunctionReturnDecoder.decode(
                        event.data,
                        blockChainConfig.transferEventParams as MutableList<TypeReference<Type<Any>>>?
                    )
                    sellOrderService.transferTokens(
                        transferData[0].value as BigInteger,
                        transferData[1].value as String,
                        transferData[2].value as BigInteger,
                    )
                },
                { err ->
                    println("Blockchain unreachable: ${err.message}")
                    Thread.sleep(10000)
                    registerSubscriber('T')
                },
            )

            'W' -> withdrawSubscriber = web3j.ethLogFlowable(withdrawFilter).subscribe(
                { event ->
                    val withdrawData = FunctionReturnDecoder.decode(
                        event.data,
                        blockChainConfig.withdrawEventParams as MutableList<TypeReference<Type<Any>>>?
                    )
                    sellOrderService.cancelSellOrder(
                        withdrawData[0].value as BigInteger,
                    )
                },
                { err ->
                    println("Blockchain unreachable: ${err.message}")
                    Thread.sleep(10000)
                    registerSubscriber('W')
                },
            )
        }
    }

    companion object {

        const val TRANSFER_EVENT = "Transfer"
        const val DEPOSIT_EVENT = "Deposit"
        const val WITHDRAW_EVENT = "Withdraw"

    }

}

private val logger = KotlinLogging.logger { }

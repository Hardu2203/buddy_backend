package com.example.buddy_backend.sell_order

import com.example.buddy_backend.config.BlockchainConfig
import com.example.buddy_backend.config.CurrencyEnum
import com.example.buddy_backend.security.AuthenticationFacade
import com.example.buddy_backend.token.TokenEnum
import mu.KotlinLogging
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.web3j.abi.datatypes.Address
import java.math.BigDecimal
import java.math.BigInteger
import java.util.Currency

@Service
class SellOrderService(
    private val blockchainConfig: BlockchainConfig,
    private val sellOrderRepository: SellOrderRepository,
    private val authenticationFacade: AuthenticationFacade,
) {

    fun createSellOrder(
        id: BigInteger,
        account: String,
        token: String,
        amount: BigInteger,
        currency: CurrencyEnum,
        price: BigInteger
    ) {
        logger.info { "check sell order exists $id" }
        if (sellOrderRepository.existsById(id)) return
        logger.info { "creating sell order $id" }
        sellOrderRepository.save(
            SellOrder(
                id,
                account,
                token,
                SellOrderStatus.ACTIVE,
                amount,
                currency,
                price,
                TokenEnum.WBTC
            )
        )
    }

    fun cancelSellOrder(id: BigInteger) {
        logger.info { "cancelling sell order $id" }
        val sellOrder = sellOrderRepository.findByIdOrNull(id) ?: error("cancel of sell order $id failed 404")
        sellOrder.status = SellOrderStatus.CANCELLED
        sellOrderRepository.save(sellOrder)
    }

    fun transferTokens(id: BigInteger, buyer: String, amount: BigInteger) {

        logger.info { "trying to transfer sell order $id" }

        if (sellOrderRepository.sellOrderIsTransferred(id)) return

        logger.info { "transferring sell order $id" }
        val sellOrder = sellOrderRepository.findByIdOrNull(id) ?: error("transfer of sell order $id failed 404")
        sellOrder.buyerPublicKey = buyer
        sellOrder.status = SellOrderStatus.COMPLETED
        sellOrderRepository.save(sellOrder)
    }

    fun getActiveSellOrdersByUser(): List<SellOrderDto> {
        return sellOrderRepository.findBySellerPublicKey(authenticationFacade.getAuthentication().principal.publicKey.lowercase()).map { it.toSellOrderDto() }
    }

}

private val logger = KotlinLogging.logger {  }

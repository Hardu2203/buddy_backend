package com.example.buddy_backend.sell_order

import com.example.buddy_backend.config.BlockchainConfig
import org.springframework.stereotype.Service
import org.web3j.abi.datatypes.Address
import java.math.BigInteger

@Service
class SellOrderService(
    private val blockchainConfig: BlockchainConfig,
    private val sellOrderRepository: SellOrderRepository,
) {

    fun createSellOrder(txHash: String, account: Address, token: Address, amount: BigInteger) {
        sellOrderRepository.save(
            SellOrder(
                txHash,
                account.value,
                token.value,
                SellOrderStatus.ACTIVE,
                amount
            )
        )
    }

    fun cancelSellOrder(account: Address, token: Address, amount: BigInteger) {
        val sellOrder = sellOrderRepository.findFirstByTokenAddressAndSellerPublicKeyAndAmountOrderBySellOrderNumberAsc(
            token.value,
            account.value,
            amount
        )
        sellOrder.status = SellOrderStatus.CANCELLED
        sellOrderRepository.save(sellOrder)
    }

    fun transferTokens(from: Address, to: Address, token: Address, amount: BigInteger) {
        val sellOrder = sellOrderRepository.findFirstByTokenAddressAndSellerPublicKeyAndAmountOrderBySellOrderNumberAsc(
            token.value,
            from.value,
            amount
        )
        sellOrder.buyerPublicKey = to.value
        sellOrder.status = SellOrderStatus.COMPLETED
        sellOrderRepository.save(sellOrder)
    }

}
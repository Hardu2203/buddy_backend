package com.example.buddy_backend.sell_order

import com.example.buddy_backend.ControllerBase
import com.example.buddy_backend.bank.BankRequestDto
import com.example.buddy_backend.bank.BankResponseDto
import com.example.buddy_backend.bank.BankService
import com.example.buddy_backend.security.AuthenticationFacade
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(ControllerBase.SELL_ORDER)
class SellOrderController(
    private val bankService: BankService,
    private val authenticationFacade: AuthenticationFacade,
    private val sellOrderService: SellOrderService
) {

    @PostMapping
    fun createOrUpdateUserBank(
        @RequestBody bankRequestDto: BankRequestDto
    ): BankResponseDto {
        return bankService.createUserBank(bankRequestDto)
    }


    @GetMapping
    fun getUserSellOrders(): List<SellOrderDto> {
        return sellOrderService.getActiveSellOrdersByUser()
    }

}

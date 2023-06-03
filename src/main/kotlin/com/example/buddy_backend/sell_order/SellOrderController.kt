package com.example.buddy_backend.sell_order

import com.example.buddy_backend.ControllerBase
import com.example.buddy_backend.bank.BankRequestDto
import com.example.buddy_backend.bank.BankResponseDto
import com.example.buddy_backend.bank.BankService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ControllerBase.SELL_ORDER)
class SellOrderController(
    private val bankService: BankService
) {

    @PostMapping
    fun createOrUpdateUserBank(
        @RequestBody bankRequestDto: BankRequestDto
    ): BankResponseDto {
        return bankService.createUserBank(bankRequestDto)
    }


}
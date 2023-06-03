package com.example.buddy_backend.bank

import com.example.buddy_backend.ControllerBase
import com.example.buddy_backend.user.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ControllerBase.BANK)
class BankController(
        val bankService: BankService,
) {

    @GetMapping("{publicKey}")
    fun getUserBank(
            @PathVariable publicKey: String
    ): BankResponseDto? {
        return bankService.getBankPerUserOrNull(publicKey)
    }

    @PostMapping
    fun createOrUpdateUserBank(
            @RequestBody bankRequestDto: BankRequestDto
    ): BankResponseDto {
        return bankService.createUserBank(bankRequestDto)
    }

}
package com.example.buddy_backend.bank

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class BankService(
        val bankRepository: BankRepository,
        val bankMapper: BankMapper,
) {

    fun getBankPerUserOrNull(publicKey: String): BankResponseDto? {
        return bankMapper.toBankResponseDto(bankRepository.findByIdOrNull(publicKey) ?: return null)
    }

    fun createUserBank(bankRequestDto: BankRequestDto): BankResponseDto {
        val userBankSaved = bankRepository.save(bankMapper.toBank(bankRequestDto))
        return bankMapper.toBankResponseDto(userBankSaved)
    }

}
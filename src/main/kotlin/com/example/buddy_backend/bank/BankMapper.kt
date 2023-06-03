package com.example.buddy_backend.bank

import com.example.buddy_backend.user.User
import com.example.buddy_backend.user.UserService
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpServerErrorException

@Mapper(uses = [BankCustomMapper::class])
interface BankMapper {

    @Mapping(source = "requestDto", target = "user")
    fun toBank(requestDto: BankRequestDto): Bank

    fun toBankResponseDto(bank: Bank): BankResponseDto
}

@Component
class BankCustomMapper(val userService: UserService) {

    fun assignUser(requestDto: BankRequestDto): User {
        return userService.getUserOrNull(requestDto.publicKey) ?: throw HttpServerErrorException(HttpStatus.NOT_FOUND, "User does not exist")
    }

}
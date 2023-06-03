package com.example.buddy_backend.user

import com.example.buddy_backend.security.SecurityService
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(uses = [SecurityService::class])
interface UserMapper {

    fun toUserResponseDto(user: User): UserResponseDto

//    @Mapping(source = null, target = "nonce", qualifiedByName = ["getNonce"])
    fun toUser(userRequestDto: UserRequestDto): User
}
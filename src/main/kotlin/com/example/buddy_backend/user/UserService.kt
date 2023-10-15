package com.example.buddy_backend.user

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.concurrent.ThreadLocalRandom

@Service
class UserService(
        val userRepository: UserRepository,
) {

    fun createUser(publicKey: String): User {
        return userRepository.save(User(publicKey, ThreadLocalRandom.current().nextLong(1000000000, 9999999999)))
    }

    fun getUserOrNull(publicKey: String): User? {
        return userRepository.findByIdOrNull(publicKey)
    }

}

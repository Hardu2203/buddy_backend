package com.example.buddy_backend.security


import com.example.buddy_backend.user.UserRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.komputing.khash.keccak.Keccak
import org.komputing.khash.keccak.KeccakParameter
import org.mapstruct.Named
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.server.ResponseStatusException
import org.web3j.crypto.ECDSASignature
import org.web3j.crypto.Sign
import java.math.BigInteger
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import org.web3j.utils.Numeric

@Component
class SecurityService(
    val userRepository: UserRepository,
    val jwtTokenProvider: JwtTokenProvider,
    @Value("\${security.jwt.token.secret-key}")
    private var secretKey: String? = null,

    @Value("\${security.jwt.token.expire-length}")
    private val validityInMilliseconds: Long = 0
) {
    fun login(publicKey: String, signedNonce: String): LoginDto {
        var match = false
        val prefix = "\u0019Ethereum Signed Message:\n50Sign this nonce to continue to sign in: "
        if (userRepository.existsById(publicKey)) {
            val nonce = userRepository.getById(publicKey).nonce.toString()
            val message = Keccak.digest((prefix + nonce).toByteArray(), KeccakParameter.KECCAK_256)
            val signatureBytes = Numeric.hexStringToByteArray(signedNonce)

            match = checkSignedNonce(signatureBytes, message, publicKey)
            if (match) {
                //val secretKey = Keys.hmacShaKeyFor(securityConfig.secretKey.encodeToByteArray())
                val jwtToken = getJwtToken(publicKey)
                val refreshToken = getRefreshToken(publicKey)
                val newNonce = ThreadLocalRandom.current().nextLong(1000000000, 9999999999)
                val user = userRepository.getById(publicKey)
                user.nonce = newNonce
                userRepository.save(user)
                return LoginDto(jwtToken, refreshToken)
            } else {
                throw ResponseStatusException(HttpStatus.FORBIDDEN, "Login failed")
            }
        }
        throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
    }

    fun loginWithRefreshToken(refreshToken: RefreshToken): String {
        if (jwtTokenProvider.validateToken(refreshToken.token)) {
            val claimsJws = jwtTokenProvider.getClaims(refreshToken.token)
            val principal = claimsJws.body.subject
            return getJwtToken(principal)
        }
        throw ResponseStatusException(HttpStatus.CONFLICT, "Invalid refresh token")
    }

    private fun checkSignedNonce(
        signatureBytes: ByteArray,
        message: ByteArray,
        publicKey: String,
    ): Boolean {
        var match1 = false
        val v = signatureBytes[64]
        if (v < 27)
            v.plus(27)
        val signatureData = Sign.SignatureData(
            v,
            signatureBytes.copyOfRange(0, 32),
            signatureBytes.copyOfRange(32, 64),
        )

        var recoveredAddress: String
        for (i in 0..3) {
            val recoveredPublicKey = Sign.recoverFromSignature(
                i,
                ECDSASignature(
                    BigInteger(1, signatureData.r),
                    BigInteger(1, signatureData.s)
                ),
                message
            )

            if (recoveredPublicKey != null) {
                recoveredAddress = "0x" + org.web3j.crypto.Keys.getAddress(recoveredPublicKey)
                if (recoveredAddress == publicKey.lowercase()) {
                    match1 = true
                    break
                }
            }
        }
        return match1
    }

    private fun getJwtToken(publicKey: String): String = Jwts.builder()
        .setSubject(publicKey)
        .setExpiration(Date(System.currentTimeMillis() + validityInMilliseconds))
        .signWith(Keys.hmacShaKeyFor(secretKey!!.toByteArray()))
        .compact()

    private fun getRefreshToken(publicKey: String): String = Jwts.builder()
        .setSubject(publicKey)
        .setExpiration(Date(System.currentTimeMillis() + validityInMilliseconds + 864000000))
        .signWith(Keys.hmacShaKeyFor(secretKey!!.toByteArray()))
        .compact()
}

data class LoginDto(
    val jwt: String,
    val refreshToken: String
)

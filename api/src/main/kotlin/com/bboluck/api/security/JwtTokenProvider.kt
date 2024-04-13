package com.bboluck.api.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.Arrays
import java.util.Date
import java.util.UUID
import java.util.stream.Collectors

enum class TokenType(val value: String) {
    ACCESS_TOKEN("access"), REFRESH_TOKEN("refresh")
}

const val ACCESS_TOKEN_VALID_HOUR: Long = 1
const val REFRESH_TOKEN_VALID_DAYS: Long = 30

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}")
    private val secret: String,
) {
    companion object {
        private const val TOKEN_TYPE = "token_type"
        private const val ISSUER = "bbo-luck.com"
        private const val ROLES = "roles"
        private const val UID = "uid"
        const val HEADER_PREFIX = "Bearer "
    }

    fun getAuthentication(token: String): Authentication {
        val claims: Claims = decodeToken(token)
        val authorities = Arrays.stream(
            claims[ROLES].toString().split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
        ).collect(Collectors.toSet())
        val uid = claims[UID] as String

        val grantedAuthorities = authorities.map { SimpleGrantedAuthority(it) }

        return UsernamePasswordAuthenticationToken(
            User(
                uid,
                token,
                grantedAuthorities
            ),
            token,
            grantedAuthorities
        )
    }

    fun generateToken(uid: String, roles: List<String>): Pair<String, String> {
        val accessTokenExpiredAt = LocalDateTime.now().plusHours(ACCESS_TOKEN_VALID_HOUR)
        val refreshTokenExpiredAt = LocalDateTime.now().plusDays(REFRESH_TOKEN_VALID_DAYS)

        return Pair(
            generateToken(Pair(uid, roles), TokenType.ACCESS_TOKEN, accessTokenExpiredAt),
            generateToken(Pair(uid, roles), TokenType.REFRESH_TOKEN, refreshTokenExpiredAt),
        )
    }

    private fun generateToken(info: Pair<String, List<String>>, tokenType: TokenType, expiredAt: LocalDateTime): String {
        val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
        val authorities: String = info.second.joinToString { "," }

        val accessToken = Jwts.builder()
            .setId(UUID.randomUUID().toString())
            .setIssuer(ISSUER)
            .setIssuedAt(Date())
            .setNotBefore(Date())
            .setExpiration(Timestamp.valueOf(expiredAt))
            .claim(TOKEN_TYPE, tokenType.value)
            .claim(UID, info.first)
            .claim(ROLES, authorities)
            .signWith(key, SignatureAlgorithm.HS256)
        return accessToken.compact()
    }

    private fun decodeToken(token: String): Claims {
        val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
    }
}

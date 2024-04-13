package com.bboluck.api.security

import com.bboluck.api.security.JwtTokenProvider.Companion.HEADER_PREFIX
import com.bboluck.common.api.logback.Log
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean

class AuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
) : GenericFilterBean(), Log {

    companion object {
        const val HEADER_TOKEN = HttpHeaders.AUTHORIZATION
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest

        val authorization = httpRequest.getHeader(HEADER_TOKEN)
        if (!authorization.isNullOrEmpty()) {
            val token = authorization.substring(authorization.indexOf(HEADER_PREFIX))

            val authentication = jwtTokenProvider.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = authentication
        }

        chain.doFilter(request, response)
    }
}

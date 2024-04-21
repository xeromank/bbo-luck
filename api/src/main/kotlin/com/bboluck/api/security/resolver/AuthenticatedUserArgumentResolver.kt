package com.bboluck.api.security.resolver

import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class AuthenticatedUserArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == AuthenticatedUserDTO::class.java &&
            parameter.hasParameterAnnotation(AuthenticatedUser::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw IllegalArgumentException("AuthenticatedUser cannot be null")

        val userDetails = authentication.principal as UserDetails
        val authorities = userDetails.authorities
        val roles = authorities.asSequence().map {
            it.authority
        }.joinToString(",")
        return AuthenticatedUserDTO(userDetails.username, roles)
    }
}

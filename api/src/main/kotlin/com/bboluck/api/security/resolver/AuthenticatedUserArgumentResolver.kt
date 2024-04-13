package com.bboluck.api.security.resolver

import org.springframework.core.MethodParameter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
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

        val user: User = authentication.principal as User
        val roles: String = (user.authorities as List<GrantedAuthority>).map { it.authority }.joinToString { "," }
        return AuthenticatedUserDTO(user.username, roles)
    }
}

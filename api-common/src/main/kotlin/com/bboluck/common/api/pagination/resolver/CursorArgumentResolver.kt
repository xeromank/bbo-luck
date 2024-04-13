package com.bboluck.common.api.pagination.resolver

import com.bboluck.common.api.pagination.dto.CursorDTO
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class CursorArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == CursorDTO::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        val request = webRequest.nativeRequest as HttpServletRequest
        val previous: String = request.getParameter("previous") ?: ""
        val next: String = request.getParameter("next") ?: ""
        val pageSize: String = request.getParameter("page_size") ?: "10"

        return CursorDTO.of(previous, next, pageSize, null)
    }
}

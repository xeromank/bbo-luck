package com.bboluck.common.api.pagination.resolver

import com.bboluck.common.api.pagination.dto.PageDTO
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class PageArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == PageDTO::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        val request = webRequest.nativeRequest as HttpServletRequest
        val pageNumber: String = if (request.getParameter("page_number").isNullOrEmpty()) {
            "0"
        } else {
            request.getParameter("page_number")
        }
        val pageSize: String = if (request.getParameter("page_size").isNullOrEmpty()) {
            "10"
        } else {
            request.getParameter("page_size")
        }

        return PageDTO.of(pageNumber, pageSize, null)
    }
}

package com.bboluck.api.security

import com.bboluck.common.api.dto.response.ResponseDTO
import com.bboluck.common.api.model.MetaCode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.catalina.connector.ClientAbortException
import org.springframework.http.HttpStatus
import org.springframework.web.filter.OncePerRequestFilter

class ExceptionHandlerFilter : OncePerRequestFilter() {

    @Suppress("TooGenericExceptionCaught")
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: JwtException) {
            logger.warn("token filter exception.")
            val status = HttpStatus.UNAUTHORIZED
            response.setErrorResponse(status, e.message)
        } catch (e: ClientAbortException) {
            // 클라이언트가 서버응답을 받기 전에 연결을 끊는 경우에 발생. 보통 소켓통신 연결이 끊어진 경우 발생.
            logger.warn("ClientAbortException", e)
        } catch (e: Exception) {
            logger.error("Internal Server Error.", e)
            val status = HttpStatus.INTERNAL_SERVER_ERROR
            response.setErrorResponse(status, e.message)
        }
    }

    private fun HttpServletResponse.setErrorResponse(status: HttpStatus, message: String?) {
        this.status = status.value()
        setHeader(this)
        val mapper = ObjectMapper()
        mapper.propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
        val dtoMetaCode = MetaCode.valueFrom(status)
        val json = mapper.writeValueAsString(
            ResponseDTO<Any>(
                meta = ResponseDTO.Meta(
                    code = dtoMetaCode,
                    type = dtoMetaCode.name,
                    message = message
                ),
            ),
        )
        this.writer.write(json)
        this.writer.flush()
        this.writer.close()
    }

    private fun setHeader(response: HttpServletResponse) {
        response.setHeader("content-type", "application/json;charset=UTF-8")
        response.setHeader("cache-control", "no-cache, no-store, max-age=0, must-revalidate")
        response.setHeader("expires", "0")
        response.setHeader("pragma", "no-cache'")
        response.setHeader("Access-Control-Allow-Origin", "*")
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With")
    }
}

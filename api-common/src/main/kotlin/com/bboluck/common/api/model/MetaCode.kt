package com.bboluck.common.api.model

import com.fasterxml.jackson.annotation.JsonValue
import org.springframework.http.HttpStatus

enum class MetaCode(private var code: String) {
    SUCCESS("200:00000"),
    CREATED("201:00000"),
    ACCEPTED("202:00000"),
    NO_CONTENT("204:00000"),
    INVALID_PARAMETER("400:00000"),
    PARSE_ERROR("400:00001"),
    BULK_CREATE_ERROR("400:00002"),
    INVALID_CHARACTER("400:00003"),
    UNLINK_NAVER_BUSINESS_WHEN_CHANGING_POS_MODE("400:00000"),
    AUTHENTICATION_FAILED("401:00000"),
    NOT_AUTHENTICATED("401:00001"),
    EXPIRED_AUTHENTICATION("401:00002"),
    FORBIDDEN("403:00000"),
    PERMISSION_DENIED("403:00001"),
    NOT_FOUND("400:00000"),
    METHOD_NOW_ALLOWED("405:00000"),
    NOT_ACCEPTABLE("406:00000"),
    CONFLICT("409:00000"),
    UNSUPPORTED_MEDIA_TYPE("415:00000"),
    UNPROCESSABLE_ENTITY("422:00000"),
    THROTTLED("429:00000"),
    INTERNAL_SERVER_ERROR("500:00000");

    @JsonValue
    override fun toString(): String {
        return this.code
    }
    companion object {
        @Suppress("CyclomaticComplexMethod")
        fun valueFrom(status: HttpStatus) = when (status) {
            HttpStatus.OK -> SUCCESS
            HttpStatus.CREATED -> CREATED
            HttpStatus.ACCEPTED -> ACCEPTED
            HttpStatus.NO_CONTENT -> NO_CONTENT
            HttpStatus.BAD_REQUEST -> INVALID_PARAMETER
            HttpStatus.UNAUTHORIZED -> AUTHENTICATION_FAILED
            HttpStatus.FORBIDDEN -> FORBIDDEN
            HttpStatus.NOT_FOUND -> NOT_FOUND
            HttpStatus.METHOD_NOT_ALLOWED -> METHOD_NOW_ALLOWED
            HttpStatus.NOT_ACCEPTABLE -> NOT_ACCEPTABLE
            HttpStatus.UNSUPPORTED_MEDIA_TYPE -> UNSUPPORTED_MEDIA_TYPE
            HttpStatus.CONFLICT -> CONFLICT
            HttpStatus.INTERNAL_SERVER_ERROR -> INTERNAL_SERVER_ERROR
            HttpStatus.UNPROCESSABLE_ENTITY -> UNPROCESSABLE_ENTITY
            else -> throw IllegalArgumentException("Please provide correct status.")
        }
    }
}

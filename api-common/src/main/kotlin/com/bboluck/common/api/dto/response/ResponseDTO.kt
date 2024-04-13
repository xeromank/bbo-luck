package com.bboluck.common.api.dto.response

import com.bboluck.common.api.model.MetaCode

open class ResponseDTO<T>(
    open val meta: Meta,
    open val data: T? = null,
) {
    companion object {
        fun <T> success(
            meta: Meta,
            data: T? = null
        ): ResponseDTO<T> =
            ResponseDTO(meta, data)
        fun <T> success(
            metaCode: MetaCode,
            data: T? = null
        ): ResponseDTO<T> =
            success(
                Meta(code = metaCode),
                data
            )
        fun <T> success(data: T? = null): ResponseDTO<T> =
            success(
                MetaCode.SUCCESS,
                data
            )
    }

    data class Meta(
        val code: MetaCode,
        val type: String? = code.name.lowercase(),
        val message: String? = null,
    )
}

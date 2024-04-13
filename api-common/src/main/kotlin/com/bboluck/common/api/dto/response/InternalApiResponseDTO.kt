package com.bboluck.common.api.dto.response

data class InternalApiResponseDTO<T>(
    val meta: Meta,
    val data: T? = null,
) {
    companion object {
        fun <T> success(
            meta: Meta,
            data: T? = null
        ): InternalApiResponseDTO<T> =
            InternalApiResponseDTO(meta, data)
        fun <T> success(
            metaCode: String,
            data: T? = null
        ): InternalApiResponseDTO<T> =
            success(
                Meta(code = metaCode),
                data
            )
        fun <T> success(data: T? = null): InternalApiResponseDTO<T> =
            success(
                "20000000",
                data
            )
    }

    data class Meta(
        val code: String,
        val type: String? = null,
        val message: String? = null,
    )
}

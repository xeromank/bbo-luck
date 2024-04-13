package com.bboluck.common.api.pagination.dto.response

import com.bboluck.common.api.dto.response.ResponseDTO
import com.bboluck.common.api.dto.response.ResponseDTO.Meta
import com.bboluck.common.api.model.MetaCode

data class CursorResponse<T> (
    override val meta: Meta,
    override val data: Collection<T>,
    val pagination: Pagination,
) : ResponseDTO<Collection<T>>(meta, data) {
    companion object {
        fun <T> cursorSuccess(
            list: List<T>,
            previous: String = "",
            next: String = ""
        ): CursorResponse<T> {
            return CursorResponse(
                Meta(MetaCode.SUCCESS),
                list,
                Pagination(
                    previous,
                    next
                )
            )
        }
    }

    data class Pagination(
        val previous: String,
        val next: String,
    )
}

package com.bboluck.common.api.pagination.dto.response

import com.bboluck.common.api.dto.response.ResponseDTO
import com.bboluck.common.api.model.MetaCode
import org.springframework.data.domain.Page

data class PageResponse<T>(
    override val meta: Meta,
    override val data: Collection<T>,
    val pagination: Pagination?,
) : ResponseDTO<Collection<T>>(meta, data) {

    companion object {
        fun <T : Any> pageSuccess(page: Page<T>): PageResponse<T> =
            PageResponse(
                Meta(MetaCode.SUCCESS),
                page.content,
                Pagination(page.number + 1, page.size, page.totalPages, page.totalElements)
            )
        fun <T : Any> pageSuccess(page: Page<T>, pagination: Boolean): PageResponse<T> =
            PageResponse(
                Meta(MetaCode.SUCCESS),
                page.content,
                takeIf { pagination }
                    ?.let { Pagination(page.number + 1, page.size, page.totalPages, page.totalElements) }
            )
    }

    data class Pagination(
        val pageNumber: Int,
        val pageSize: Int,
        val totalPage: Int,
        val totalCount: Long,
    )
}

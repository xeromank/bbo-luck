package com.bboluck.common.api.pagination.dto

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

open class PageDTO(
    currentPageNumber: Int = 0,
    currentPageSize: Int = 10,
    sortOrder: Sort = Sort.unsorted(),
) : PageRequest(currentPageNumber, currentPageSize, sortOrder) {
    companion object {
        fun of(pageNumber: String, pageSize: String, sort: Sort?): PageDTO {
            val pageNum: Int = if (pageNumber.toInt() <= 0) {
                0
            } else {
                pageNumber.toInt() - 1
            }
            return PageDTO(pageNum, pageSize.toInt(), sort ?: Sort.unsorted())
        }
    }
}

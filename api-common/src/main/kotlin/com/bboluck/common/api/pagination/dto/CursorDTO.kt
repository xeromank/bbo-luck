package com.bboluck.common.api.pagination.dto

import org.apache.commons.lang3.StringUtils
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import java.time.LocalDateTime
import java.util.Base64

data class CursorDTO(
    val next: String,
    val previous: String,
    val currentPageSize: Int = 10,
    val sortOrder: Sort = Sort.unsorted(),
) : PageRequest(0, currentPageSize, sortOrder) {
    companion object {
        fun of(
            previous: String,
            next: String,
            pageSize: String,
            sort: Sort?
        ): CursorDTO {
            return CursorDTO(next, previous, pageSize.toInt(), sort ?: Sort.unsorted())
        }
    }

    fun decodedCursor(cursorValue: String?): String {
        require(cursorValue != null) { "Cursor value is not valid!" }
        val decodedBytes = Base64.getDecoder().decode(cursorValue)
        val decodedValue = String(decodedBytes)
        return StringUtils.substringBetween(decodedValue, "###")
    }

    fun encodedCursor(field: String, hasMoreElements: Boolean): String {
        if (!hasMoreElements) return ""

        val structuredValue = "###" + field + "### - " + LocalDateTime.now()
        return Base64.getEncoder().encodeToString(structuredValue.encodeToByteArray())
    }

    fun hasCursors(): Boolean {
        return hasPrevPageCursor() || hasNextPageCursor()
    }

    fun hasNextPageCursor(): Boolean {
        return !next.isNullOrEmpty()
    }

    fun hasPrevPageCursor(): Boolean {
        return !previous.isNullOrEmpty()
    }

    fun getSearchValue(): String {
        if (!hasCursors()) return ""
        return if (hasPrevPageCursor()) decodedCursor(previous) else decodedCursor(next)
    }
}

package com.bboluck.common.api.logback

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface Log {
    object HttpLog {
        const val REQUEST_ID = "request-id"
        const val REMOTE_SERVICE = "remote-service"
        const val METHOD = "method"
        const val URL = "url"
        const val HEADERS = "headers"
        const val PARAMETERS = "parameters"
        const val CONTENT_TYPE = "content-type"
        const val REQUEST = "request"
        const val RESPONSE = "response"
        const val STATUS = "status"
        const val X_REQUEST_ID = "x-request-id"
    }

    object EventLog {
        const val EVENT_NAME = "event-name"
        const val EVENT_VALUE = "event-value"
    }
    val logger: Logger get() = LoggerFactory.getLogger(this.javaClass)

    companion object {
        private val objectMapper: ObjectMapper =
            ObjectMapper().apply { setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY) }
    }

    fun Logger.info(map: Map<String, Any>) {
        this.info(objectMapper.writeValueAsString(map))
    }

    fun Logger.jsonObjLogFormat(key: String, obj: Any) {
        this.info(objectMapper.writeValueAsString(mapOf("type" to key, "data" to obj)))
    }

    fun Logger.error(map: Map<String, Any>) {
        this.error(objectMapper.writeValueAsString(map))
    }

    fun Logger.jsonLogFormat(contentAsByteArray: ByteArray?): Any {
        if (contentAsByteArray == null || contentAsByteArray.isEmpty()) return ""
        return try {
            objectMapper.readValue(contentAsByteArray, Any::class.java)
        } catch (e: JsonParseException) {
            String(contentAsByteArray, Charsets.UTF_8)
        }
    }
}

package com.bboluck.common.api.utils

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule

class StringTrimJacksonModule : SimpleModule() {
    init {
        addDeserializer(String::class.java, StringTrimDeserializer())
    }
}

class StringTrimDeserializer : JsonDeserializer<String?>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): String? {
        return p.text?.trim()
    }
}

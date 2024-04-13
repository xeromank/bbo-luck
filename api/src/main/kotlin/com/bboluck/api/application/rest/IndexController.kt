package com.bboluck.api.application.rest

import com.bboluck.common.api.dto.response.ResponseDTO.Companion.success
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class IndexController {

    @GetMapping("/")
    fun index() = success("Bbo Luck API")
}

package com.bboluck.api.application.rest

import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    @GetMapping("/user/test")
    fun user(authentication: Authentication): String {
        return authentication.name
    }
}

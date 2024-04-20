package com.bboluck.api

import com.bboluck.api.domain.service.UserService
import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BboLuckAPIApplication(
    private val userService: UserService
) {

    @PostConstruct
    fun init() {
        userService.makeTestUser()
    }
}

fun main(args: Array<String>) {
    runApplication<BboLuckAPIApplication>(*args)
}

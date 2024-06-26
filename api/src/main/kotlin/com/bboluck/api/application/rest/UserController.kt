package com.bboluck.api.application.rest

import com.bboluck.api.security.resolver.AuthenticatedUser
import com.bboluck.api.security.resolver.AuthenticatedUserDTO
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

// 사용자 정보 조회
const val GET_USER = "/api/v1/user"

// 이벤트 참여 기록 조회
const val GET_USER_EVENT_HISTORY = "/api/v1/user/events"

// 응모권 관련 기록 조회
const val GET_USER_TICKET_HISTORY = "/api/v1/user/tickets"

@RestController
class UserController {

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(GET_USER)
    fun user(
        @AuthenticatedUser
        user: AuthenticatedUserDTO,
    ): String {
        return user.username
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseBody
    @GetMapping(GET_USER_EVENT_HISTORY)
    fun getUserEventsHistory(
        @AuthenticatedUser
        user: AuthenticatedUserDTO,
    ): Pair<String, String?> {
        return Pair(user.username, user.roles)
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseBody
    @GetMapping(GET_USER_TICKET_HISTORY)
    fun getUserTicketHistory(
        @AuthenticatedUser
        user: AuthenticatedUserDTO,
    ): Pair<String, String?> {
        return Pair(user.username, user.roles)
    }
}

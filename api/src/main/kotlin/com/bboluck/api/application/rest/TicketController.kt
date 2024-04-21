package com.bboluck.api.application.rest

import com.bboluck.api.security.resolver.AuthenticatedUser
import com.bboluck.api.security.resolver.AuthenticatedUserDTO
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

// 응모권 발행(광고시청 후)
const val POST_PUBLISH_TICKET = "/api/v1/ticket"

// 응모권 발행 체크(광고 시청 가능 여부)
const val GET_POSSIBLE_PUBLISH_CHECK = "/api/v1/ticket/possible/check"

@RestController
class TicketController {

    @PreAuthorize("hasAuthority('USER')")
    @ResponseBody
    @PostMapping(POST_PUBLISH_TICKET)
    fun publicTicket(
        @AuthenticatedUser
        user: AuthenticatedUserDTO,
        @PathVariable eventId: String
    ): Pair<String, String?> {
        return Pair(user.username, user.roles)
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseBody
    @GetMapping(GET_POSSIBLE_PUBLISH_CHECK)
    fun checkPossiblePublish(
        @AuthenticatedUser
        user: AuthenticatedUserDTO,
        @PathVariable eventId: String
    ): Pair<String, String?> {
        return Pair(user.username, user.roles)
    }
}

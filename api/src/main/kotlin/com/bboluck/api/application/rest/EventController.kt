package com.bboluck.api.application.rest

import com.bboluck.api.security.resolver.AuthenticatedUser
import com.bboluck.api.security.resolver.AuthenticatedUserDTO
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody

// 이벤트 생성
const val POST_MAKE_EVENT = "/api/v1/event"

// 이벤트 참여(응모권 소진)
const val POST_PARTICIPATE_EVENT = "/api/v1/event/{eventId}"

// 이벤트 목록 조회
const val GET_EVENTS = "/api/v1/events"

@Controller
class EventController {
    @PreAuthorize("hasAuthority('STAFF')")
    @ResponseBody
    @PostMapping(POST_MAKE_EVENT)
    fun makeEvent(
        @AuthenticatedUser
        user: AuthenticatedUserDTO
    ): Pair<String, String?> {
        return Pair(user.username, user.roles)
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseBody
    @PostMapping(POST_PARTICIPATE_EVENT)
    fun participateEvent(
        @AuthenticatedUser
        user: AuthenticatedUserDTO,
        @PathVariable eventId: String
    ): Pair<String, String?> {
        return Pair(user.username, user.roles)
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseBody
    @GetMapping(GET_EVENTS)
    fun getEvent(
        @AuthenticatedUser
        user: AuthenticatedUserDTO,
    ): Pair<String, String?> {
        return Pair(user.username, user.roles)
    }
}

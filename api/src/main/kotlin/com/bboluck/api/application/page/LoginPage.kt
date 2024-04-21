package com.bboluck.api.application.page

import com.bboluck.api.security.resolver.AuthenticatedUser
import com.bboluck.api.security.resolver.AuthenticatedUserDTO
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class LoginPage {

    @GetMapping("/page/login")
    fun getLoginPage(model: Model): String {
        model.addAttribute("data", "데이터")

        return "login"
    }

    @PreAuthorize("hasAuthority('USER')")
    @ResponseBody
    @GetMapping("/test/user")
    fun testUser(
        @AuthenticatedUser
        user: AuthenticatedUserDTO
    ): Pair<String, String?> {
        return Pair(user.username, user.roles)
    }

    @PreAuthorize("hasAuthority('STAFF')")
    @ResponseBody
    @GetMapping("/test/staff")
    fun testStaff(
        @AuthenticatedUser
        user: AuthenticatedUserDTO
    ): Pair<String, String?> {
        return Pair(user.username, user.roles)
    }
}

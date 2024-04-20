package com.bboluck.api.application.page

import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
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

    @ResponseBody
    @GetMapping("/test/login")
    fun testLogin(authentication: Authentication): String {
        println(authentication.principal)
        val oAuth2User = authentication.principal as OAuth2User
        println(oAuth2User.attributes)
        return "1234"
    }
}

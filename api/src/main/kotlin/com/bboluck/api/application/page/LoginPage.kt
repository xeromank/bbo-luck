package com.bboluck.api.application.page

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LoginPage {

    @GetMapping("/page/login")
    fun loginPage(model: Model): String {
        model.addAttribute("data","데이터")

        return "login"
    }
}

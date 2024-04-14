package com.bboluck.api.config

import com.bboluck.api.security.resolver.AuthenticatedUserArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.http.CacheControl
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(AuthenticatedUserArgumentResolver())
    }

}

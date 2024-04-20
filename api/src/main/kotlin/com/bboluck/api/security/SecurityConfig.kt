package com.bboluck.api.security

import com.bboluck.api.security.service.BboLuckOAuth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider,
    private val bboLuckOAuth2UserService: BboLuckOAuth2UserService,
) {

    @Bean
    fun encodePwd(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests {
                it
                    .requestMatchers("/health/*", "/docs/*", "/swagger-ui/*", "/api-docs", "/error", "/page/login")
                    .permitAll()
            }
        http
            .authorizeHttpRequests {
                it
                    .requestMatchers("/user/**").authenticated()
                    .anyRequest().permitAll()
            }
        http
            .formLogin { formLogin ->
                formLogin
                    .loginPage("/page/login")
                    .usernameParameter("email")
                    .loginProcessingUrl("/login-process")
                    .defaultSuccessUrl("/")
            }
            .logout { logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/page/login")
            }
            .csrf { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            }
            .addFilterBefore(AuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(ExceptionHandlerFilter(), AuthenticationFilter::class.java)

        http.oauth2Login {
            it.loginPage("/page/login")
            it.failureUrl("/page/login")
            it.defaultSuccessUrl("/")
            it.userInfoEndpoint { endpointConfig ->
                endpointConfig.userService(bboLuckOAuth2UserService)
            }
        }

        return http.build()
    }
}

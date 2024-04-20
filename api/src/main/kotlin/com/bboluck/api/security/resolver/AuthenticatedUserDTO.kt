package com.bboluck.api.security.resolver

import com.bboluck.api.domain.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

data class AuthenticatedUserDTO(
    val uid: String,
    val roles: String? = null,
    val oAuthAttributes: MutableMap<String, Any> = mutableMapOf()
) : UserDetails, OAuth2User {

    private lateinit var password: String

    constructor(user: User) : this(user.userId.toString(), "STAFF") {
        password = user.password!!
    }
    fun isStaff(): Boolean {
        return roles?.contains("STAFF") ?: false
    }

    override fun getName(): String {
        return uid
    }

    override fun getAttributes(): MutableMap<String, Any> {
        return oAuthAttributes
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return uid
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val collect: MutableCollection<GrantedAuthority> = ArrayList()
        roles?.split(",")?.map { GrantedAuthority { it } }?.let { collect.addAll(it) }
        return collect
    }
}

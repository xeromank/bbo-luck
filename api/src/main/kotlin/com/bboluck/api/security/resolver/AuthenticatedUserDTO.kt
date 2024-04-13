package com.bboluck.api.security.resolver

data class AuthenticatedUserDTO(
    val uid: String,
    val roles: String? = null,
) {
    fun isStaff(): Boolean {
        return roles?.contains("STAFF") ?: false
    }
}

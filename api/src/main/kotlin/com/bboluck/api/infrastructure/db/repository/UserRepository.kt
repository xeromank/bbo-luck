package com.bboluck.api.infrastructure.db.repository

import com.bboluck.api.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Int> {
    fun findUserByEmail(email: String): User?
}

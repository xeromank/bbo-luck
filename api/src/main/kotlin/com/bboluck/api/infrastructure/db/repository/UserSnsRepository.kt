package com.bboluck.api.infrastructure.db.repository

import com.bboluck.api.domain.entity.SnsUser
import org.springframework.data.jpa.repository.JpaRepository

interface UserSnsRepository : JpaRepository<SnsUser, Int> {
    fun findSnsUserByProviderAndOauthId(
        provider: String,
        oauthId: String
    ): SnsUser?
}

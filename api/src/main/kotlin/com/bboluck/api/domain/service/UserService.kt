package com.bboluck.api.domain.service

import com.bboluck.api.domain.entity.User
import com.bboluck.api.infrastructure.db.repository.UserRepository
import com.bboluck.api.infrastructure.db.repository.UserSnsRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userSnsRepository: UserSnsRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
) {

    @Transactional
    fun makeTestUser() {
        val user = User(
            email = "xeroman.k@gmail.com",
            username = "xero",
            birthDate = "2000-02-28",
            nickname = "xeroman.k",
            password = bCryptPasswordEncoder.encode("1234")
        )

        userRepository.save(user)
    }
}

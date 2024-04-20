package com.bboluck.api.security.service

import com.bboluck.api.infrastructure.db.repository.UserRepository
import com.bboluck.api.security.resolver.AuthenticatedUserDTO
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@Service
class BboLuckUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findUserByEmail(username)
            ?: throw UsernameNotFoundException("User not found")

        return AuthenticatedUserDTO(
            user = user
        )
    }
}

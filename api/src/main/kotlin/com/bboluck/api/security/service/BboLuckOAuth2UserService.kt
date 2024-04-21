package com.bboluck.api.security.service

import com.bboluck.api.domain.entity.SnsUser
import com.bboluck.api.domain.entity.User
import com.bboluck.api.infrastructure.db.repository.UserRepository
import com.bboluck.api.infrastructure.db.repository.UserSnsRepository
import com.bboluck.api.security.resolver.AuthenticatedUserDTO
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BboLuckOAuth2UserService(
    private val userRepository: UserRepository,
    private val userSnsRepository: UserSnsRepository,
) : DefaultOAuth2UserService() {

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)

        val userNameAttributeName = userRequest.clientRegistration.providerDetails.userInfoEndpoint.userNameAttributeName
        val provider = userRequest.clientRegistration.registrationId
        val providerId = oAuth2User.getAttribute<String>(userNameAttributeName)
            ?: throw IllegalArgumentException("providerId is null")

        var userId = userSnsRepository.findSnsUserByProviderAndOauthId(provider, providerId)?.userId
        val email = oAuth2User.getAttribute<String>("email") ?: ""
        val name = oAuth2User.getAttribute<String>("name") ?: ""

        if (userId == null) {
            userId = userRepository.findUserByEmail(email)?.userId
            if (userId != null) {
                makeSnsUser(userId, provider, providerId)
            }
        }

        if (userId == null) {
            userId = makeUser(email, name, provider, providerId)
        }

        return AuthenticatedUserDTO(
            userId.toString(),
            "USER",
            oAuth2User.attributes
        )
    }

    @Transactional
    fun makeSnsUser(
        userId: Int,
        provider: String,
        providerId: String
    ) {
        val snsUser = SnsUser(
            provider = provider,
            oauthId = providerId,
            userId = userId
        )
        userSnsRepository.save(snsUser)
    }

    @Transactional
    fun makeUser(
        email: String,
        name: String,
        provider: String,
        providerId: String,
    ): Int {
        val user = User(
            email = email,
            username = name,
            password = null,
            birthDate = null,
            nickname = null,
        )

        val saveUser = userRepository.save(user)
        makeSnsUser(saveUser.userId!!, provider, providerId)
        return saveUser.userId!!
    }
}

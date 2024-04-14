package com.bboluck.api.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.Comment
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime


@Entity
@Table(name = "bboluck_sns_users",
    indexes = [Index(unique = true, columnList = "oauth_id, oauth_provider")])
class SnsUser(
    @Column(name = "user_id")
    @Comment("사용자 ID FK")
    var userId: Int,
    @Column(name = "oauth_provider")
    @Comment("OAuth 공급자")
    var provider: String,
    @Column(name = "oauth_id")
    @Comment("Oauth ID")
    var oauthId: String,
) {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        referencedColumnName = "user_id",
        insertable = false,
        updatable = false
    )
    var user: User? = null

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var snsUserId: Long? = null

    @CreationTimestamp
    @CreatedDate
    var createdAt: LocalDateTime = LocalDateTime.now()

    @UpdateTimestamp
    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.now()
}

package com.bboluck.api.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.Comment
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

@Entity
@Table(name = "bboluck_users")
class User(

    @Comment("사용자 이메일")
    @Column(name = "email", nullable = false, length = 50)
    var email: String,

    @Comment("사용자 이름")
    @Column(name = "username", nullable = false, length = 40)
    var username: String,

    @Comment("사용자 생년월일")
    @Column(name = "birth_date", nullable = false, length = 10)
    var birthDate: String,

    @Comment("사용자 닉네임")
    @Column(name = "nickname", nullable = false, length = 40)
    var nickname: String,

) {
    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    var userId: Int? = null

    @CreationTimestamp
    @CreatedDate
    var createdAt: LocalDateTime = LocalDateTime.now()

    @UpdateTimestamp
    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.now()
}

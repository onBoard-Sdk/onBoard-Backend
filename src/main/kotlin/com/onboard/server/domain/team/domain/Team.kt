package com.onboard.server.domain.team.domain

import com.onboard.server.global.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Team(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(nullable = false, unique = true, columnDefinition = "varchar(60)")
    val email: String,

    @Column(nullable = false, columnDefinition = "char(60)")
    val password: String,

    @Column(nullable = false, columnDefinition = "varchar(50)")
    val name: String,

    @Column(nullable = false)
    val logoImageUrl: String,
) : BaseEntity() {
}

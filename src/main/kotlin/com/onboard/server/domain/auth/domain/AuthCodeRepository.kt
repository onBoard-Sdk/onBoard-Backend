package com.onboard.server.domain.auth.domain

import org.springframework.data.repository.CrudRepository

interface AuthCodeRepository : CrudRepository<AuthCode, String> {
    fun findAllByEmail(email: String): List<AuthCode>
}

package com.onboard.server.domain.auth.repository

import com.onboard.server.domain.auth.domain.AuthCode
import org.springframework.data.repository.CrudRepository

interface AuthCodeRepository : CrudRepository<AuthCode, String> {
    fun findAllByEmail(email: String): List<AuthCode>
}

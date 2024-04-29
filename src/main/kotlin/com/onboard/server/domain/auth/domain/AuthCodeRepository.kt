package com.onboard.server.domain.auth.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

interface AuthCodeRepository : CrudRepository<AuthCode, String> {
}

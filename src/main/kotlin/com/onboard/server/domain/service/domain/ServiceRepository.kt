package com.onboard.server.domain.service.domain

import org.springframework.data.jpa.repository.JpaRepository

interface ServiceRepository : JpaRepository<Service, Long> {
}
package com.training.case36.bank.adapter.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AccountJpaRepository : JpaRepository<AccountJpaEntity, UUID>

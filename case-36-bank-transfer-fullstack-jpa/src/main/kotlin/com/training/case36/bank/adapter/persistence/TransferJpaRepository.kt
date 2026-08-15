package com.training.case36.bank.adapter.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TransferJpaRepository : JpaRepository<TransferJpaEntity, UUID>

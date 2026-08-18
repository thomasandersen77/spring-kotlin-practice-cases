package com.training.case36.bank.adapter.persistence

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface TransferJpaRepository : JpaRepository<TransferJpaEntity, UUID>

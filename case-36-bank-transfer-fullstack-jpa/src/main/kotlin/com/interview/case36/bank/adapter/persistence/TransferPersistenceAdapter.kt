package com.interview.case36.bank.adapter.persistence

import com.interview.case36.bank.application.port.TransferRepository
import com.interview.case36.bank.domain.BankTransfer
import org.springframework.stereotype.Repository

@Repository
class TransferPersistenceAdapter(
    private val transferJpaRepository: TransferJpaRepository
) : TransferRepository {

    /**
     * TODO 6: Map the transfer to an entity, save it, and map the saved entity back to the domain.
     */
    override fun save(transfer: BankTransfer): BankTransfer =
        transferJpaRepository.save(transfer.toEntity()).toDomain()

    override fun count(): Long = transferJpaRepository.count()
}

package com.interview.case41.loyalty

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class LoyaltyPointsServiceTest {

    @Mock
    lateinit var customerRepository: CustomerRepository

    @Mock
    lateinit var pointsLedger: PointsLedger

    @InjectMocks
    lateinit var loyaltyPointsService: LoyaltyPointsService

    @Test
    fun `TODO 1 - standard customer only gets basic points without bonus points`() {
        // arrange
        val amountOre = 1_000L

        val customer = Customer(
            id = UUID.randomUUID().toString(),
            tier = CustomerTier.STANDARD
        )


        Mockito.`when`(customerRepository.findById(customer.id))
            .thenReturn(customer)

        // act
        val pointsAward = loyaltyPointsService.awardForPurchase(customer.id, amountOre)

        // assert
        assertThat(pointsAward.totalPoints).isEqualTo(1)
        assertThat(pointsAward.tierBonusPoints).isEqualTo(0)
        assertThat(pointsAward.basePoints).isEqualTo(1)

        verify(pointsLedger).credit(customer.id, pointsAward.totalPoints)
    }
}
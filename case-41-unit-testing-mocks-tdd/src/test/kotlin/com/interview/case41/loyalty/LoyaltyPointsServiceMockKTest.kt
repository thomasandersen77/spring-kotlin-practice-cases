package com.interview.case41.loyalty

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import java.util.*

@ExtendWith(MockKExtension::class)
class LoyaltyPointsServiceMockKTest {

    val customerRepository: CustomerRepository = mockk()

    val pointsLedger: PointsLedger = mockk()
    val loyaltyPointsService = LoyaltyPointsService(customerRepository, pointsLedger)

    @Test
    fun `standard customer only gets basic points without bonus points`() {
        // arrange
        val amountOre = 1_000L

        val customer = Customer(
            id = UUID.randomUUID().toString(),
            tier = CustomerTier.STANDARD
        )

        every { customerRepository.findById(customer.id) } returns customer
        every { pointsLedger.credit(customer.id, any()) } returns Unit

        // act
        val pointsAward = loyaltyPointsService.awardForPurchase(customer.id, amountOre)

        // assert
        assertThat(pointsAward.totalPoints).isEqualTo(1)
        assertThat(pointsAward.tierBonusPoints).isEqualTo(0)
        assertThat(pointsAward.basePoints).isEqualTo(1)

    }

    @Test
    fun `plus customer gets base points and equal bonus points`() {
        // arrange
        val amountOre = 2_000L

        val customer = Customer(
            id = UUID.randomUUID().toString(),
            tier = CustomerTier.PLUS
        )

        `when`(customerRepository.findById(customer.id))
            .thenReturn(customer)

        // act
        val pointsAward = loyaltyPointsService.awardForPurchase(customer.id, amountOre)

        // assert
        assertThat(pointsAward.totalPoints).isEqualTo(4)
        assertThat(pointsAward.tierBonusPoints).isEqualTo(2)
        assertThat(pointsAward.basePoints).isEqualTo(2)

        verify(pointsLedger).credit(customer.id, pointsAward.totalPoints)
    }

    @Test
    fun `unknown customer throws CustomerNotFoundException`() {
        // arrange
        val amountOre = 1_000L

        val customer = Customer(
            id = UUID.randomUUID().toString(),
            tier = CustomerTier.STANDARD
        )

        `when`(customerRepository.findById(customer.id))
            .thenReturn(null)

        // act + assert
        assertThatExceptionOfType(CustomerNotFoundException::class.java)
            .isThrownBy { loyaltyPointsService.awardForPurchase(customer.id, amountOre) }
            .withMessage("Fant ikke kunde ${customer.id}")
    }

    @Test
    fun `negative purchase amount throws IllegalArgumentException`() {
        val amountOre = -500L
        val customer = Customer(
            id = UUID.randomUUID().toString(),
            tier = CustomerTier.STANDARD
        )

        assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy {
                loyaltyPointsService.awardForPurchase(customer.id, amountOre)
            }.withMessage("amountOre må være større enn 0")

        verifyNoInteractions(pointsLedger)
    }

    @Test
    fun `purchase below point threshold is not credited`() {
        val amountOre = 500L
        val customer = Customer(
            id = UUID.randomUUID().toString(),
            tier = CustomerTier.PLUS
        )

        `when`(customerRepository.findById(customer.id))
            .thenReturn(customer)

        val pointsAward = loyaltyPointsService.awardForPurchase(customer.id, amountOre)

        assertThat(pointsAward.totalPoints).isEqualTo(0)
        assertThat(pointsAward.basePoints).isEqualTo(0)
        assertThat(pointsAward.tierBonusPoints).isEqualTo(0)

        verifyNoInteractions(pointsLedger)
    }

    @Test
    fun `missing customerId throws IllegalArgumentException`() {
        val amountOre = 1_000L
        val customer = Customer(
            id = "  ", // blank customer id
            tier = CustomerTier.PLUS
        )

        assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy { loyaltyPointsService.awardForPurchase(customer.id, amountOre) }
            .withMessage("customerId kan ikke være blank")


        verifyNoInteractions(pointsLedger)
    }

}
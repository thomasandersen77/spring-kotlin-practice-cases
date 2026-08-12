package com.interview.case41.loyalty

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
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

        verify(exactly = 1) {
            pointsLedger.credit(customer.id, pointsAward.totalPoints)
        }

    }

    @Test
    fun `plus customer gets base points and equal bonus points`() {
        // arrange
        val amountOre = 2_000L

        val customer = Customer(
            id = UUID.randomUUID().toString(),
            tier = CustomerTier.PLUS
        )

        every { customerRepository.findById(customer.id) } returns customer
        every { pointsLedger.credit(any(), any()) } returns Unit

        // act
        val pointsAward = loyaltyPointsService.awardForPurchase(customer.id, amountOre)

        // assert
        assertThat(pointsAward.totalPoints).isEqualTo(4)
        assertThat(pointsAward.tierBonusPoints).isEqualTo(2)
        assertThat(pointsAward.basePoints).isEqualTo(2)

        verify(exactly = 1) {
            pointsLedger.credit(customer.id, pointsAward.totalPoints)
        }
    }

    @Test
    fun `unknown customer throws CustomerNotFoundException`() {
        // arrange
        val amountOre = 1_000L

        val customer = Customer(
            id = UUID.randomUUID().toString(),
            tier = CustomerTier.STANDARD
        )

        every { customerRepository.findById(customer.id) } returns null

        // act + assert
        assertThatExceptionOfType(CustomerNotFoundException::class.java)
            .isThrownBy { loyaltyPointsService.awardForPurchase(customer.id, amountOre) }
            .withMessage("Fant ikke kunde ${customer.id}")

        verify(exactly = 0) {
            pointsLedger.credit(any<String>(), any<Int>())
        }
    }

    @Test
    fun `invalid purchase amount throws IllegalArgumentException`() {
        val invalidPurchaseAmount = 0L
        val amountOre = invalidPurchaseAmount
        val customer = Customer(
            id = UUID.randomUUID().toString(),
            tier = CustomerTier.STANDARD
        )

        assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy {
                loyaltyPointsService.awardForPurchase(customer.id, amountOre)
            }.withMessage("amountOre må være større enn 0")

        verify(exactly = 0) {
            pointsLedger.credit(any<String>(), any<Int>())
        }
    }

    @Test
    fun `purchase below point threshold is not credited`() {
        val amountOre = 500L
        val customer = Customer(
            id = UUID.randomUUID().toString(),
            tier = CustomerTier.PLUS
        )
        every { customerRepository.findById(customer.id) } returns customer

        val pointsAward = loyaltyPointsService.awardForPurchase(customer.id, amountOre)

        assertThat(pointsAward.totalPoints).isEqualTo(0)
        assertThat(pointsAward.basePoints).isEqualTo(0)
        assertThat(pointsAward.tierBonusPoints).isEqualTo(0)

        verify(exactly = 0) {
            pointsLedger.credit(any(), any())
        }
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


        verify(exactly = 0) {
            pointsLedger.credit(any(), any())
        }
    }

    @Test
    fun `PLUS purchase of 49_999 ore should still give double total points`() {
        val amountOre = 49_999L
        val customer = Customer(
            id = "123",
            tier = CustomerTier.PLUS
        )

        every { customerRepository.findById(customer.id) } returns customer
        justRun { pointsLedger.credit(customer.id, any<Int>()) }

        val pointsAward = loyaltyPointsService.awardForPurchase(customer.id, amountOre)

        assertThat(pointsAward.basePoints).isEqualTo(49)
        assertThat(pointsAward.tierBonusPoints).isEqualTo(49)
        assertThat(pointsAward.totalPoints).isEqualTo(98)

        verify(exactly = 1) {
            pointsLedger.credit(customer.id, pointsAward.totalPoints)
        }

    }

    @Test
    fun `PLUS purchase of at least 50 000 ore should give three times the total points instead of double`() {
        val amountOre = 50_000L // 500 kr
        val customer = Customer(
            id = "1234",
            tier = CustomerTier.PLUS
        )

        every { customerRepository.findById(customer.id) } returns customer
        justRun { pointsLedger.credit(customer.id, any()) }

        val pointsAward = loyaltyPointsService.awardForPurchase(customer.id, amountOre)

        assertThat(pointsAward.totalPoints).isEqualTo(150)
        assertThat(pointsAward.basePoints).isEqualTo(50)
        assertThat(pointsAward.tierBonusPoints).isEqualTo(100)

        verify(exactly = 1) {
            pointsLedger.credit(customer.id, pointsAward.totalPoints)
        }
    }
}


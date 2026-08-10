package com.interview.case41.loyalty

import io.mockk.every
import io.mockk.junit5.MockKExtension
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
            pointsLedger.credit(customer.id, 1)
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

        verify(exactly = 1){
            pointsLedger.credit(any(), any())
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
        every { pointsLedger.credit(customer.id, 0) } returns Unit

        val pointsAward = loyaltyPointsService.awardForPurchase(customer.id, amountOre)

        assertThat(pointsAward.totalPoints).isEqualTo(0)
        assertThat(pointsAward.basePoints).isEqualTo(0)
        assertThat(pointsAward.tierBonusPoints).isEqualTo(0)

        verify(exactly = 0) {
            // Explanation of this verify function with lambda described below. Do not retract points because of these comments:
            /*
                When verifying, use eq(..) function, not plain values. If not, it will fail.
                I could use any<Type>(), but I use that in the testcase below "missing customer ID from an illegal argument"
                This is the Kotlin doc's for eq(..)
                    * If matchers are being used, the  eq argument matcher must be used to match literal values.
                    * When no matchers are used, literal arguments are automatically matched using eq.
             */
            /*
                This is a learning exercise, so I want to use both to remember. When it comes to production, I think I will use this version to be most specific.
                With values that are the correct values and not any value
             */
            pointsLedger.credit(eq(customer.id), eq(0))
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


        verify( exactly = 0) {
            pointsLedger.credit(any(), any())
        }
    }

}
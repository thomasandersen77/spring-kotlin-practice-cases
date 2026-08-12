package com.interview.case41.loyalty

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class LoyaltyPointsServiceMockitoTest {

    @Mock
    lateinit var customerRepository: CustomerRepository

    @Mock
    lateinit var pointsLedger: PointsLedger

    @InjectMocks
    lateinit var loyaltyPointsService: LoyaltyPointsService

    @Test
    fun `standard customer only gets basic points without bonus points`() {
        // arrange
        val amountOre = 1_000L

        val customer = Customer(
            id = UUID.randomUUID().toString(),
            tier = CustomerTier.STANDARD
        )


        `when`(customerRepository.findById(customer.id))
            .thenReturn(customer)

        // act
        val pointsAward = loyaltyPointsService.awardForPurchase(customer.id, amountOre)

        // assert
        assertThat(pointsAward.totalPoints).isEqualTo(1)
        assertThat(pointsAward.tierBonusPoints).isEqualTo(0)
        assertThat(pointsAward.basePoints).isEqualTo(1)

        verify(pointsLedger).credit(customer.id, pointsAward.totalPoints)
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

        verifyNoInteractions(pointsLedger)
    }

    @Test
    fun `invalid' purchase amount throws IllegalArgumentException`() {
        val amountOre = 0L

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

    @Test
    fun `PLUS purchase of 49_999 ore should still give double total points`() {
        val amountOre = 49_999L
        val customer = Customer(
            id = "123",
            tier = CustomerTier.PLUS
        )

        `when`(customerRepository.findById(customer.id))
            .thenReturn(customer)

        val pointsAward = loyaltyPointsService.awardForPurchase(customer.id, amountOre)

        assertThat(pointsAward.basePoints).isEqualTo(49)
        assertThat(pointsAward.tierBonusPoints).isEqualTo(49)
        assertThat(pointsAward.totalPoints).isEqualTo(98)

        verify(pointsLedger)
            .credit(customer.id, pointsAward.totalPoints)

    }

    @Test
    fun `PLUS purchase of at least 50 000 ore should give three times the total points instead of double`() {
        val amountOre = 50_000L // 500 kr
        val customer = Customer(
            id = "1234",
            tier = CustomerTier.PLUS
        )

        `when`(customerRepository.findById(customer.id))
            .thenReturn(customer)

        val pointsAward = loyaltyPointsService.awardForPurchase(customer.id, amountOre)

        assertThat(pointsAward.totalPoints).isEqualTo(150)
        assertThat(pointsAward.basePoints).isEqualTo(50)
        assertThat(pointsAward.tierBonusPoints).isEqualTo(100)

        verify(pointsLedger)
            .credit(customer.id, pointsAward.totalPoints)
    }

}
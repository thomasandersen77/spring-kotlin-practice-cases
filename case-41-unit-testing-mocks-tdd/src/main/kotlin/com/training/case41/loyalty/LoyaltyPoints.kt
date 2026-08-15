package com.training.case41.loyalty

enum class CustomerTier {
 STANDARD,
 PLUS
}

data class Customer(
 val id: String,
 val tier: CustomerTier
)

data class PointsAward(
 val customerId: String,
 val basePoints: Int,
 val tierBonusPoints: Int
) {
 val totalPoints: Int = basePoints + tierBonusPoints
}

fun interface CustomerRepository {
 fun findById(customerId: String): Customer?
}

fun interface PointsLedger {
 fun credit(customerId: String, points: Int)
}

class CustomerNotFoundException(customerId: String) :
 RuntimeException("Fant ikke kunde $customerId")

class LoyaltyPointsService(
 private val customerRepository: CustomerRepository,
 private val pointsLedger: PointsLedger
) {
 fun awardForPurchase(customerId: String, amountOre: Long): PointsAward {
 require(customerId.isNotBlank()) { "customerId kan ikke være blank" }
 require(amountOre > 0) { "amountOre må være større enn 0" }

 val customer = customerRepository.findById(customerId)
 ?: throw CustomerNotFoundException(customerId)
 val basePoints = (amountOre / ORE_PER_POINT).toInt()
 val tierBonusPoints = when (customer.tier) {
 CustomerTier.STANDARD -> 0
 CustomerTier.PLUS -> basePoints
 }
 val award = PointsAward(customer.id, basePoints, tierBonusPoints)

 if (award.totalPoints > 0) {
 pointsLedger.credit(customer.id, award.totalPoints)
 }

 return award
 }

 private companion object {
 const val ORE_PER_POINT = 1_000L
 }
}

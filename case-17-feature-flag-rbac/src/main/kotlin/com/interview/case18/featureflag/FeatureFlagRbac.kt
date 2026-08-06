package com.interview.case18.featureflag

enum class Role {
    ADMIN,
    PRODUCT_OWNER,
    DEVELOPER
}

enum class Environment {
    DEV,
    STAGING,
    PRODUCTION
}

data class CurrentUser(val userId: String, val role: Role, val productArea: String?)
data class FeatureFlag(val name: String, val productArea: String, val experimental: Boolean)

class FeatureFlagAccessPolicy {
    fun canActivate(user: CurrentUser, flag: FeatureFlag, environment: Environment, approvedForProd: Boolean): Boolean {
        require(user.userId.isNotBlank()) { "user id cannot be blank" }
        require(flag.name.isNotBlank() && flag.productArea.isNotBlank()) { "flag identity cannot be blank" }

        if (environment == Environment.PRODUCTION && (!approvedForProd || flag.experimental)) return false
        if (user.role == Role.ADMIN) return true
        if (user.productArea.isNullOrBlank() || user.productArea != flag.productArea) return false

        return when (user.role) {
            Role.ADMIN -> true
            Role.PRODUCT_OWNER -> environment in setOf(Environment.DEV, Environment.STAGING, Environment.PRODUCTION)
            Role.DEVELOPER -> environment == Environment.DEV
        }
    }
}

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
        TODO("Implement RBAC and approval rules")
    }
}

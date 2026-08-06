package com.interview.case18.featureflag

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FeatureFlagRbacTest {
    private val policy = FeatureFlagAccessPolicy()

    @Test
    fun `developer should activate flag in dev`() {
        val user = CurrentUser(userId = "u-1", role = Role.DEVELOPER, productArea = "search")
        val flag = FeatureFlag(name = "new-ranking", productArea = "search", experimental = false)

        val allowed = policy.canActivate(user, flag, Environment.DEV, approvedForProd = false)

        assertThat(allowed).isTrue()
    }

    @Test
    fun `developer should be limited to own product area and dev`() {
        val user = CurrentUser("u", Role.DEVELOPER, "search")
        val own = FeatureFlag("flag", "search", false)

        assertThat(policy.canActivate(user, own, Environment.STAGING, false)).isFalse()
        assertThat(policy.canActivate(user, own.copy(productArea = "billing"), Environment.DEV, false)).isFalse()
        assertThat(policy.canActivate(user.copy(productArea = null), own, Environment.DEV, false)).isFalse()
    }

    @Test
    fun `product owner can use own area but production requires approval`() {
        val user = CurrentUser("u", Role.PRODUCT_OWNER, "search")
        val flag = FeatureFlag("flag", "search", false)

        assertThat(policy.canActivate(user, flag, Environment.STAGING, false)).isTrue()
        assertThat(policy.canActivate(user, flag, Environment.PRODUCTION, false)).isFalse()
        assertThat(policy.canActivate(user, flag, Environment.PRODUCTION, true)).isTrue()
    }

    @Test
    fun `experimental flag cannot be activated in production even by admin`() {
        val admin = CurrentUser("admin", Role.ADMIN, null)
        val flag = FeatureFlag("experiment", "search", true)

        assertThat(policy.canActivate(admin, flag, Environment.DEV, false)).isTrue()
        assertThat(policy.canActivate(admin, flag, Environment.PRODUCTION, true)).isFalse()
    }
}

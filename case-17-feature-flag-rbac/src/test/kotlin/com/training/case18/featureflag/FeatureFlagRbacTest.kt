package com.training.case18.featureflag

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
}

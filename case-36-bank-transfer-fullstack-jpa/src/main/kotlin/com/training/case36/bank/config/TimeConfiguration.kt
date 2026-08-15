package com.training.case36.bank.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

/**
 * Exposes a system [Clock] as a Spring bean so [com.training.case36.bank.application.BankingService]
 * never calls `Instant.now()` directly. Tests can then override this bean with a fixed clock to get
 * deterministic `executedAt` timestamps on transfers.
 */
@Configuration
class TimeConfiguration {

 @Bean
 fun clock(): Clock = Clock.systemUTC()
}

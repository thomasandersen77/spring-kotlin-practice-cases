package com.interview.case32.notifications

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.Instant

class NotificationDomainTest {

    private val fullContact = Recipient(
        id = "R1",
        email = "kari@example.com",
        msisdn = "99887766",
        marketingConsent = true
    )
    private val withoutSmsAndConsent = Recipient(
        id = "R2",
        email = "ola@example.com",
        msisdn = null,
        marketingConsent = false
    )
    private val withoutEmail = Recipient(
        id = "R3",
        email = null,
        msisdn = "40404040",
        marketingConsent = true
    )
    private val withoutContactInfo = Recipient(
        id = "R4",
        email = null,
        msisdn = null,
        marketingConsent = true
    )

    private val orderShipped = Notification.OrderShipped("R1", "ORD-1", "https://sporing/ORD-1")
    private val paymentFailed = Notification.PaymentFailed("R1", "ORD-1", amountNok = 499)
    private val passwordReset =
        Notification.PasswordReset("R1", "token-123", Instant.parse("2026-08-03T20:00:00Z"))
    private val marketing = Notification.MarketingCampaign("R1", "CAMP-1", "Sommersalg")

    @Test
    fun `passordvarsel sendes bare pa epost, og ikke i appen`() {
        assertThat(NotificationPolicy.channelsFor(passwordReset, fullContact))
            .containsExactly(Channel.Email("kari@example.com"))

        assertThat(NotificationPolicy.channelsFor(passwordReset, withoutEmail)).isEmpty()
    }

    @Test
    fun `betalingsfeil sendes pa alle tilgjengelige kanaler med SMS forst`() {
        assertThat(NotificationPolicy.channelsFor(paymentFailed, fullContact))
            .containsExactly(
                Channel.Sms("99887766"),
                Channel.Email("kari@example.com"),
                Channel.InApp
            )

        assertThat(NotificationPolicy.channelsFor(paymentFailed, withoutSmsAndConsent))
            .containsExactly(Channel.Email("ola@example.com"), Channel.InApp)

        assertThat(NotificationPolicy.channelsFor(paymentFailed, withoutContactInfo))
            .containsExactly(Channel.InApp)
    }

    @Test
    fun `sendingsvarsel bruker epost og app, men aldri SMS`() {
        assertThat(NotificationPolicy.channelsFor(orderShipped, fullContact))
            .containsExactly(Channel.Email("kari@example.com"), Channel.InApp)

        assertThat(NotificationPolicy.channelsFor(orderShipped, withoutEmail))
            .containsExactly(Channel.InApp)
    }

    @Test
    fun `markedsforing krever bade samtykke og epost`() {
        assertThat(NotificationPolicy.channelsFor(marketing, fullContact))
            .containsExactly(Channel.Email("kari@example.com"))

        assertThat(NotificationPolicy.channelsFor(marketing, withoutSmsAndConsent)).isEmpty()
        assertThat(NotificationPolicy.channelsFor(marketing, withoutEmail)).isEmpty()
    }

    @Test
    fun `prioritet folger varseltype`() {
        assertThat(NotificationPolicy.priorityOf(passwordReset)).isEqualTo(Priority.HIGH)
        assertThat(NotificationPolicy.priorityOf(paymentFailed)).isEqualTo(Priority.HIGH)
        assertThat(NotificationPolicy.priorityOf(orderShipped)).isEqualTo(Priority.NORMAL)
        assertThat(NotificationPolicy.priorityOf(marketing)).isEqualTo(Priority.LOW)
    }

    @Test
    fun `avvisningsarsak forklarer hvorfor ingenting kunne sendes`() {
        assertThat(NotificationPolicy.rejectionReasonFor(orderShipped, fullContact)).isNull()

        assertThat(NotificationPolicy.rejectionReasonFor(marketing, withoutSmsAndConsent))
            .isEqualTo(RejectionReason.MARKETING_CONSENT_MISSING)

        assertThat(NotificationPolicy.rejectionReasonFor(marketing, withoutEmail))
            .isEqualTo(RejectionReason.MISSING_CONTACT_INFO)

        assertThat(NotificationPolicy.rejectionReasonFor(passwordReset, withoutEmail))
            .isEqualTo(RejectionReason.MISSING_CONTACT_INFO)
    }

    @Test
    fun `kanaler har lesbare navn`() {
        assertThat(Channel.Email("kari@example.com").label).isEqualTo("e-post")
        assertThat(Channel.Sms("99887766").label).isEqualTo("SMS")
        assertThat(Channel.InApp.label).isEqualTo("app")
    }

    @Test
    fun `leveringsresultat beskrives ulikt per variant`() {
        val delivered = DeliveryResult.Delivered(Channel.Email("kari@example.com"), "ref-1")
        val rejected = DeliveryResult.Rejected(Channel.Sms("99887766"), RejectionReason.MISSING_CONTACT_INFO)
        val retryable = DeliveryResult.Retryable(Channel.InApp, Duration.ofSeconds(30), "timeout")

        assertThat(describe(delivered)).isEqualTo("Levert via e-post (ref=ref-1)")
        assertThat(describe(rejected)).isEqualTo("Avvist via SMS: MISSING_CONTACT_INFO")
        assertThat(describe(retryable)).isEqualTo("Nytt forsøk via app om 30 s")
    }

    @Test
    fun `rapport teller varianter og finner korteste ventetid`() {
        val results = listOf(
            DeliveryResult.Delivered(Channel.Email("kari@example.com"), "ref-1"),
            DeliveryResult.Delivered(Channel.InApp, "ref-2"),
            DeliveryResult.Rejected(Channel.Sms("99887766"), RejectionReason.MISSING_CONTACT_INFO),
            DeliveryResult.Retryable(Channel.Email("kari@example.com"), Duration.ofSeconds(60), "5xx"),
            DeliveryResult.Retryable(Channel.InApp, Duration.ofSeconds(30), "timeout")
        )

        val report = results.toReport()

        assertThat(report.deliveredCount).isEqualTo(2)
        assertThat(report.rejectedCount).isEqualTo(1)
        assertThat(report.retryableCount).isEqualTo(2)
        assertThat(report.nextRetryAfter).isEqualTo(Duration.ofSeconds(30))
    }

    @Test
    fun `rapport uten resultater har ingen ny forsokstid`() {
        val report = emptyList<DeliveryResult>().toReport()

        assertThat(report.deliveredCount).isZero()
        assertThat(report.rejectedCount).isZero()
        assertThat(report.retryableCount).isZero()
        assertThat(report.nextRetryAfter).isNull()
    }

    @Test
    fun `varsler sorteres pa prioritet og beholder rekkefolgen innenfor samme prioritet`() {
        val sorted = listOf(marketing, orderShipped, passwordReset, paymentFailed)
            .highestPriorityFirst()

        assertThat(sorted).containsExactly(passwordReset, paymentFailed, orderShipped, marketing)
    }
}

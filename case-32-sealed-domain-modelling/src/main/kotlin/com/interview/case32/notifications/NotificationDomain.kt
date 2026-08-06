package com.interview.case32.notifications

import java.time.Duration
import java.time.Instant

/**
 * SEALED HIERARKIER SOM DOMENEMODELL
 *
 * Tre sealed hierarkier: hva som skal varsles (`Notification`), hvor det kan sendes (`Channel`),
 * og hva som skjedde (`DeliveryResult`). Reglene skal uttrykkes som `when`-uttrykk uten `else`,
 * slik at kompilatoren sier fra når en ny variant legges til.
 *
 * Reglene under implementerer kontrakten som beskrives i README og testene.
 */

// ---------- Hva skal varsles ----------

sealed interface Notification {
    val recipientId: String

    data class OrderShipped(
        override val recipientId: String,
        val orderId: String,
        val trackingUrl: String
    ) : Notification

    data class PaymentFailed(
        override val recipientId: String,
        val orderId: String,
        val amountNok: Int
    ) : Notification

    data class PasswordReset(
        override val recipientId: String,
        val resetToken: String,
        val expiresAt: Instant
    ) : Notification

    data class MarketingCampaign(
        override val recipientId: String,
        val campaignId: String,
        val subject: String
    ) : Notification
}

// ---------- Hvor kan det sendes ----------

sealed interface Channel {
    data class Email(val address: String) : Channel
    data class Sms(val msisdn: String) : Channel

    /** Ingen data — derfor `data object`, ikke `data class`. */
    data object InApp : Channel
}

data class Recipient(
    val id: String,
    val email: String?,
    val msisdn: String?,
    val marketingConsent: Boolean
)

enum class Priority {
    HIGH,
    NORMAL,
    LOW
}

// ---------- Hva skjedde ----------

sealed interface DeliveryResult {
    val channel: Channel

    data class Delivered(
        override val channel: Channel,
        val providerReference: String
    ) : DeliveryResult

    data class Rejected(
        override val channel: Channel,
        val reason: RejectionReason
    ) : DeliveryResult

    data class Retryable(
        override val channel: Channel,
        val retryAfter: Duration,
        val cause: String
    ) : DeliveryResult
}

enum class RejectionReason {
    MISSING_CONTACT_INFO,
    MARKETING_CONSENT_MISSING
}

data class DeliveryReport(
    val deliveredCount: Int,
    val rejectedCount: Int,
    val retryableCount: Int,
    val nextRetryAfter: Duration?
)

// ---------- Regler (din jobb) ----------

/**
 * Regel 1: Extension property med `when` uten `else`.
 * Kontrakt: Email -> "e-post", Sms -> "SMS", InApp -> "app".
 */
val Channel.label: String
    get() = when (this) {
        is Channel.Email -> "e-post"
        is Channel.Sms -> "SMS"
        Channel.InApp -> "app"
    }

object NotificationPolicy {

    /**
     * Regel 2: Hvilke kanaler skal brukes? `when` over `Notification` uten `else`.
     * Kontrakt (rekkefølgen i listen er en del av kontrakten):
     * - PasswordReset  -> [Email] hvis mottakeren har e-post, ellers tom liste (ingen InApp: brukeren er utestengt)
     * - PaymentFailed  -> [Sms, Email, InApp] — de kanalene mottakeren faktisk har, InApp alltid
     * - OrderShipped   -> [Email, InApp] — Email bare hvis den finnes, InApp alltid
     * - MarketingCampaign -> [Email] bare hvis mottakeren har både samtykke og e-post, ellers tom liste
     */
    fun channelsFor(notification: Notification, recipient: Recipient): List<Channel> =
        when (notification) {
            is Notification.PasswordReset -> listOfNotNull(
                recipient.email?.let(Channel::Email)
            )

            is Notification.PaymentFailed -> listOfNotNull(
                recipient.msisdn?.let(Channel::Sms),
                recipient.email?.let(Channel::Email),
                Channel.InApp
            )

            is Notification.OrderShipped -> listOfNotNull(
                recipient.email?.let(Channel::Email),
                Channel.InApp
            )

            is Notification.MarketingCampaign ->
                if (recipient.marketingConsent) {
                    listOfNotNull(recipient.email?.let(Channel::Email))
                } else {
                    emptyList()
                }
        }

    /**
     * Regel 3: Prioritet som `when`-uttrykk.
     * Kontrakt: PasswordReset og PaymentFailed -> HIGH, OrderShipped -> NORMAL, MarketingCampaign -> LOW.
     */
    fun priorityOf(notification: Notification): Priority =
        when (notification) {
            is Notification.PasswordReset,
            is Notification.PaymentFailed -> Priority.HIGH

            is Notification.OrderShipped -> Priority.NORMAL
            is Notification.MarketingCampaign -> Priority.LOW
        }

    /**
     * Regel 4: Hvorfor ble ingenting sendt?
     * Kontrakt:
     * - null hvis `channelsFor` gir minst én kanal
     * - MARKETING_CONSENT_MISSING hvis det er en MarketingCampaign uten samtykke
     * - MISSING_CONTACT_INFO ellers
     */
    fun rejectionReasonFor(notification: Notification, recipient: Recipient): RejectionReason? {
        if (channelsFor(notification, recipient).isNotEmpty()) return null

        return when (notification) {
            is Notification.MarketingCampaign ->
                if (!recipient.marketingConsent) {
                    RejectionReason.MARKETING_CONSENT_MISSING
                } else {
                    RejectionReason.MISSING_CONTACT_INFO
                }

            is Notification.PasswordReset,
            is Notification.PaymentFailed,
            is Notification.OrderShipped -> RejectionReason.MISSING_CONTACT_INFO
        }
    }
}

/**
 * Regel 5: Menneskelesbar beskrivelse. `when` over `DeliveryResult` uten `else`, med smart casts.
 * Kontrakt:
 * - Delivered -> "Levert via <label> (ref=<providerReference>)"
 * - Rejected  -> "Avvist via <label>: <reason>"
 * - Retryable -> "Nytt forsøk via <label> om <sekunder> s"
 */
fun describe(result: DeliveryResult): String =
    when (result) {
        is DeliveryResult.Delivered ->
            "Levert via ${result.channel.label} (ref=${result.providerReference})"

        is DeliveryResult.Rejected ->
            "Avvist via ${result.channel.label}: ${result.reason}"

        is DeliveryResult.Retryable ->
            "Nytt forsøk via ${result.channel.label} om ${result.retryAfter.seconds} s"
    }

/**
 * Regel 6: Oppsummer en batch med resultater.
 * Kontrakt:
 * - tellere per variant
 * - nextRetryAfter = korteste `retryAfter` blant Retryable, eller null hvis ingen kan prøves igjen
 * Hint: `filterIsInstance`, `count { }`, `minOfOrNull`.
 */
fun List<DeliveryResult>.toReport(): DeliveryReport =
    DeliveryReport(
        deliveredCount = count { it is DeliveryResult.Delivered },
        rejectedCount = count { it is DeliveryResult.Rejected },
        retryableCount = count { it is DeliveryResult.Retryable },
        nextRetryAfter = filterIsInstance<DeliveryResult.Retryable>()
            .minOfOrNull(DeliveryResult.Retryable::retryAfter)
    )

/**
 * Regel 7: Sorter varsler slik at de viktigste kommer først, og bevar innbyrdes rekkefølge
 * innenfor samme prioritet (stabil sortering).
 */
fun List<Notification>.highestPriorityFirst(): List<Notification> =
    sortedBy { notification -> NotificationPolicy.priorityOf(notification).ordinal }

package com.training.case34.registration

import java.time.LocalDate

/**
 * NULL-SAFETY OG VALIDERING
 *
 * `RegistrationForm` er rådata fra et webskjema: alt er nullable, alt kan være blankt,
 * og datoer er strenger. `Customer` er den validerte domenetypen der bare `phone` og
 * `referralCode` fortsatt kan være null — fordi de faktisk er valgfrie.
 *
 * Validatoren skal samle ALLE feil, ikke stoppe på den første, og aldri kaste exception
 * for forventet ugyldig input. Ingen `!!`.
 *
 * Se README for TODO-liste, læringsmål, valideringsregler og akseptansekriterier.
 */

data class RegistrationForm(
 val email: String?,
 val fullName: String?,
 val phone: String?,
 val birthDate: String?,
 val postalCode: String?,
 val marketingConsent: Boolean?,
 val referralCode: String?
)

data class Customer(
 val email: String,
 val fullName: String,
 val phone: String?,
 val birthDate: LocalDate,
 val postalCode: String,
 val marketingConsent: Boolean,
 val referralCode: String?
)

data class FieldError(
 val field: String,
 val message: String
)

sealed interface ValidationResult<out T> {
 data class Valid<out T>(val value: T) : ValidationResult<T>
 data class Invalid(val errors: List<FieldError>) : ValidationResult<Nothing>
}

// ---------- Hjelpefunksjoner (din jobb) ----------

/**
 * TODO 1: Trimmet verdi hvis strengen finnes og ikke er blank, ellers `null`.
 * Hint: `?.trim()?.takeIf { it.isNotEmpty() }` — én expression body, ingen if/else.
 */
fun String?.normalizedOrNull(): String? =
 TODO("Implementer normalisering av nullable/blank string")

/**
 * TODO 2: Parse ISO-dato (`yyyy-MM-dd`) uten å kaste exception. Returner `null` ved ugyldig input.
 * Hint: `runCatching { }.getOrNull()`.
 */
fun String?.toLocalDateOrNull(): LocalDate? =
 TODO("Implementer trygg datoparsing")

// ---------- Validering (din jobb) ----------

class RegistrationValidator(private val today: LocalDate) {

 /**
 * TODO 3: Valider skjemaet og bygg `Customer`.
 * Returner `ValidationResult.Valid` med normaliserte verdier, eller `ValidationResult.Invalid`
 * med ALLE feil (maks én per felt, sortert på feltnavn asc).
 * Reglene og de eksakte feilmeldingene står i README.
 */
 fun validate(form: RegistrationForm): ValidationResult<Customer> =
 TODO("Implementer validering med feilakkumulering")
}

/**
 * TODO 4: Bare de skjemaene som validerer skal bli `Customer`. Hint: `mapNotNull`.
 */
fun List<RegistrationForm>.validCustomers(validator: RegistrationValidator): List<Customer> =
 TODO("Implementer mapping av gyldige skjemaer")

/**
 * TODO 5: Alle feil fra alle skjemaer, i skjemarekkefølge. Hint: `flatMap` + `filterIsInstance`.
 */
fun List<RegistrationForm>.allErrors(validator: RegistrationValidator): List<FieldError> =
 TODO("Implementer samling av alle valideringsfeil")

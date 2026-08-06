package com.interview.case34.registration

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
 * Se README for læringsmål, valideringsregler og akseptansekriterier.
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
 * Regel 1: Trimmet verdi hvis strengen finnes og ikke er blank, ellers `null`.
 * Hint: `?.trim()?.takeIf { it.isNotEmpty() }` — én expression body, ingen if/else.
 */
fun String?.normalizedOrNull(): String? =
    this?.trim()?.takeIf(String::isNotEmpty)

/**
 * Regel 2: Parse ISO-dato (`yyyy-MM-dd`) uten å kaste exception. Returner `null` ved ugyldig input.
 * Hint: `runCatching { }.getOrNull()`.
 */
fun String?.toLocalDateOrNull(): LocalDate? =
    normalizedOrNull()?.let { value ->
        runCatching { LocalDate.parse(value) }.getOrNull()
    }

// ---------- Validering (din jobb) ----------

class RegistrationValidator(private val today: LocalDate) {

    /**
     * Regel 3: Valider skjemaet og bygg `Customer`.
     * Returner `ValidationResult.Valid` med normaliserte verdier, eller `ValidationResult.Invalid`
     * med ALLE feil (maks én per felt, sortert på feltnavn asc).
     * Reglene og de eksakte feilmeldingene står i README.
     */
    fun validate(form: RegistrationForm): ValidationResult<Customer> {
        val email = validateEmail(form.email)
        val fullName = validateFullName(form.fullName)
        val birthDate = validateBirthDate(form.birthDate)
        val postalCode = validatePostalCode(form.postalCode)
        val phone = validatePhone(form.phone)

        val errors = listOfNotNull(
            email.error,
            fullName.error,
            birthDate.error,
            postalCode.error,
            phone.error
        ).sortedBy(FieldError::field)

        if (errors.isNotEmpty()) return ValidationResult.Invalid(errors)

        return ValidationResult.Valid(
            Customer(
                email = requireNotNull(email.value),
                fullName = requireNotNull(fullName.value),
                phone = phone.value,
                birthDate = requireNotNull(birthDate.value),
                postalCode = requireNotNull(postalCode.value),
                marketingConsent = form.marketingConsent ?: false,
                referralCode = form.referralCode.normalizedOrNull()?.uppercase()
            )
        )
    }

    private fun validateEmail(rawEmail: String?): FieldValidation<String> {
        val email = rawEmail.normalizedOrNull()?.lowercase()
            ?: return FieldValidation.invalid("email", "E-post er påkrevd")

        val parts = email.split('@')
        val valid = parts.size == 2 &&
            parts[0].isNotEmpty() &&
            parts[1].split('.').let { domainParts ->
                domainParts.size >= 2 && domainParts.all(String::isNotEmpty)
            }

        return if (valid) {
            FieldValidation.valid(email)
        } else {
            FieldValidation.invalid("email", "E-post er ugyldig")
        }
    }

    private fun validateFullName(rawFullName: String?): FieldValidation<String> {
        val fullName = rawFullName.normalizedOrNull()
            ?: return FieldValidation.invalid("fullName", "Navn er påkrevd")
        val normalizedName = fullName.replace(WHITESPACE, " ")

        return if (normalizedName.length >= 2) {
            FieldValidation.valid(normalizedName)
        } else {
            FieldValidation.invalid("fullName", "Navn må ha minst 2 tegn")
        }
    }

    private fun validateBirthDate(rawBirthDate: String?): FieldValidation<LocalDate> {
        val birthDateInput = rawBirthDate.normalizedOrNull()
            ?: return FieldValidation.invalid("birthDate", "Fødselsdato er påkrevd")
        val birthDate = birthDateInput.toLocalDateOrNull()
            ?: return FieldValidation.invalid(
                "birthDate",
                "Fødselsdato må være på formatet yyyy-MM-dd"
            )

        return if (!birthDate.plusYears(18).isAfter(today)) {
            FieldValidation.valid(birthDate)
        } else {
            FieldValidation.invalid("birthDate", "Kunden må være minst 18 år")
        }
    }

    private fun validatePostalCode(rawPostalCode: String?): FieldValidation<String> {
        val postalCode = rawPostalCode.normalizedOrNull()
            ?: return FieldValidation.invalid("postalCode", "Postnummer er påkrevd")

        return if (POSTAL_CODE.matches(postalCode)) {
            FieldValidation.valid(postalCode)
        } else {
            FieldValidation.invalid("postalCode", "Postnummer må være fire siffer")
        }
    }

    private fun validatePhone(rawPhone: String?): FieldValidation<String?> {
        val phone = rawPhone.normalizedOrNull()?.replace(WHITESPACE, "")
            ?: return FieldValidation.valid(null)

        return if (PHONE_NUMBER.matches(phone)) {
            FieldValidation.valid(phone)
        } else {
            FieldValidation.invalid("phone", "Telefonnummer må være åtte siffer")
        }
    }

    private companion object {
        val WHITESPACE = Regex("\\s+")
        val POSTAL_CODE = Regex("[0-9]{4}")
        val PHONE_NUMBER = Regex("[0-9]{8}")
    }
}

/**
 * Regel 4: Bare de skjemaene som validerer skal bli `Customer`. Hint: `mapNotNull`.
 */
fun List<RegistrationForm>.validCustomers(validator: RegistrationValidator): List<Customer> =
    mapNotNull { form ->
        when (val result = validator.validate(form)) {
            is ValidationResult.Valid -> result.value
            is ValidationResult.Invalid -> null
        }
    }

/**
 * Regel 5: Alle feil fra alle skjemaer, i skjemarekkefølge. Hint: `flatMap` + `filterIsInstance`.
 */
fun List<RegistrationForm>.allErrors(validator: RegistrationValidator): List<FieldError> =
    map(validator::validate)
        .filterIsInstance<ValidationResult.Invalid>()
        .flatMap(ValidationResult.Invalid::errors)

private data class FieldValidation<out T>(
    val value: T?,
    val error: FieldError?
) {
    companion object {
        fun <T> valid(value: T): FieldValidation<T> = FieldValidation(value, null)

        fun invalid(field: String, message: String): FieldValidation<Nothing> =
            FieldValidation(null, FieldError(field, message))
    }
}

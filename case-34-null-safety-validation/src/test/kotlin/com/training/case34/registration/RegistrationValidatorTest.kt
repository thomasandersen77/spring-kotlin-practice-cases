package com.training.case34.registration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class RegistrationValidatorTest {

 private val today: LocalDate = LocalDate.of(2026, 8, 3)
 private val validator = RegistrationValidator(today)

 private val validForm = RegistrationForm(
 email = " KARI@Example.NO ",
 fullName = " Kari Nordmann ",
 phone = " 998 87 766 ",
 birthDate = "1990-05-17",
 postalCode = "0150",
 marketingConsent = null,
 referralCode = " vår2026 "
 )

 @Suppress("UNCHECKED_CAST")
 private fun customerOf(form: RegistrationForm): Customer {
 val result = validator.validate(form)
 assertThat(result).isInstanceOf(ValidationResult.Valid::class.java)
 return (result as ValidationResult.Valid<Customer>).value
 }

 private fun errorsOf(form: RegistrationForm): List<FieldError> {
 val result = validator.validate(form)
 assertThat(result).isInstanceOf(ValidationResult.Invalid::class.java)
 return (result as ValidationResult.Invalid).errors
 }

 @Test
 fun `normalizedOrNull gir trimmet verdi eller null`() {
 assertThat(null.normalizedOrNull()).isNull()
 assertThat("".normalizedOrNull()).isNull()
 assertThat(" ".normalizedOrNull()).isNull()
 assertThat(" Kari ".normalizedOrNull()).isEqualTo("Kari")
 }

 @Test
 fun `toLocalDateOrNull parser ISO-dato og svelger ugyldig input`() {
 assertThat("2026-01-31".toLocalDateOrNull()).isEqualTo(LocalDate.of(2026, 1, 31))
 assertThat("31.01.2026".toLocalDateOrNull()).isNull()
 assertThat("2026-02-30".toLocalDateOrNull()).isNull()
 assertThat(" ".toLocalDateOrNull()).isNull()
 assertThat(null.toLocalDateOrNull()).isNull()
 }

 @Test
 fun `gyldig skjema blir kunde med normaliserte verdier`() {
 val customer = customerOf(validForm)

 assertThat(customer.email).isEqualTo("kari@example.no")
 assertThat(customer.fullName).isEqualTo("Kari Nordmann")
 assertThat(customer.phone).isEqualTo("99887766")
 assertThat(customer.birthDate).isEqualTo(LocalDate.of(1990, 5, 17))
 assertThat(customer.postalCode).isEqualTo("0150")
 assertThat(customer.marketingConsent).isFalse()
 assertThat(customer.referralCode).isEqualTo("VÅR2026")
 }

 @Test
 fun `samtykke beholdes nar det er satt`() {
 assertThat(customerOf(validForm.copy(marketingConsent = true)).marketingConsent).isTrue()
 assertThat(customerOf(validForm.copy(marketingConsent = false)).marketingConsent).isFalse()
 }

 @Test
 fun `tomt skjema gir en feil per pakrevd felt, sortert pa feltnavn`() {
 val emptyForm = RegistrationForm(
 email = null,
 fullName = null,
 phone = null,
 birthDate = null,
 postalCode = null,
 marketingConsent = null,
 referralCode = null
 )

 val errors = errorsOf(emptyForm)

 assertThat(errors.map { it.field })
 .containsExactly("birthDate", "email", "fullName", "postalCode")
 assertThat(errors).contains(FieldError("email", "E-post er påkrevd"))
 assertThat(errors).contains(FieldError("fullName", "Navn er påkrevd"))
 assertThat(errors).contains(FieldError("birthDate", "Fødselsdato er påkrevd"))
 assertThat(errors).contains(FieldError("postalCode", "Postnummer er påkrevd"))
 }

 @Test
 fun `hvert felt gir maks en feil`() {
 val errors = errorsOf(validForm.copy(email = " "))

 assertThat(errors).containsExactly(FieldError("email", "E-post er påkrevd"))
 }

 @Test
 fun `ugyldig epost avvises`() {
 assertThat(errorsOf(validForm.copy(email = "kari.example.no")))
 .containsExactly(FieldError("email", "E-post er ugyldig"))

 assertThat(errorsOf(validForm.copy(email = "kari@example")))
 .containsExactly(FieldError("email", "E-post er ugyldig"))

 assertThat(errorsOf(validForm.copy(email = "@example.no")))
 .containsExactly(FieldError("email", "E-post er ugyldig"))
 }

 @Test
 fun `navn ma ha minst to tegn`() {
 assertThat(errorsOf(validForm.copy(fullName = " K ")))
 .containsExactly(FieldError("fullName", "Navn må ha minst 2 tegn"))
 }

 @Test
 fun `ugyldig datoformat gir egen feilmelding`() {
 assertThat(errorsOf(validForm.copy(birthDate = "17.05.1990")))
 .containsExactly(FieldError("birthDate", "Fødselsdato må være på formatet yyyy-MM-dd"))
 }

 @Test
 fun `kunden ma vaere myndig pa dagens dato`() {
 val exactlyEighteen = validForm.copy(birthDate = "2008-08-03")
 val dayTooYoung = validForm.copy(birthDate = "2008-08-04")

 assertThat(customerOf(exactlyEighteen).birthDate).isEqualTo(LocalDate.of(2008, 8, 3))
 assertThat(errorsOf(dayTooYoung))
 .containsExactly(FieldError("birthDate", "Kunden må være minst 18 år"))
 }

 @Test
 fun `postnummer ma vaere fire siffer`() {
 assertThat(errorsOf(validForm.copy(postalCode = "015")))
 .containsExactly(FieldError("postalCode", "Postnummer må være fire siffer"))

 assertThat(errorsOf(validForm.copy(postalCode = "01A0")))
 .containsExactly(FieldError("postalCode", "Postnummer må være fire siffer"))
 }

 @Test
 fun `telefon er valgfri, men ma ha atte siffer nar den er oppgitt`() {
 assertThat(customerOf(validForm.copy(phone = null)).phone).isNull()
 assertThat(customerOf(validForm.copy(phone = " ")).phone).isNull()

 assertThat(errorsOf(validForm.copy(phone = "12345")))
 .containsExactly(FieldError("phone", "Telefonnummer må være åtte siffer"))
 }

 @Test
 fun `referansekode normaliseres til store bokstaver eller null`() {
 assertThat(customerOf(validForm.copy(referralCode = null)).referralCode).isNull()
 assertThat(customerOf(validForm.copy(referralCode = " ")).referralCode).isNull()
 assertThat(customerOf(validForm.copy(referralCode = " host-25 ")).referralCode)
 .isEqualTo("HOST-25")
 }

 @Test
 fun `flere feil samles i samme resultat`() {
 val errors = errorsOf(
 validForm.copy(
 email = "ugyldig",
 postalCode = "1",
 phone = "999"
 )
 )

 assertThat(errors.map { it.field }).containsExactly("email", "phone", "postalCode")
 }

 @Test
 fun `bare gyldige skjemaer blir kunder i en batch`() {
 val forms = listOf(
 validForm,
 validForm.copy(email = null),
 validForm.copy(fullName = "Ola Hansen", email = "ola@example.no")
 )

 val customers = forms.validCustomers(validator)

 assertThat(customers.map { it.email }).containsExactly("kari@example.no", "ola@example.no")
 }

 @Test
 fun `alle feil fra en batch samles i skjemarekkefolge`() {
 val forms = listOf(
 validForm,
 validForm.copy(email = null),
 validForm.copy(postalCode = "99")
 )

 val errors = forms.allErrors(validator)

 assertThat(errors).containsExactly(
 FieldError("email", "E-post er påkrevd"),
 FieldError("postalCode", "Postnummer må være fire siffer")
 )
 }
}

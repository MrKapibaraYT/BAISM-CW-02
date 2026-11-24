package com.example.baism_cw_03.validation

import com.example.baism_cw_03.validation.Validators.validateAddress
import com.example.baism_cw_03.validation.Validators.validateBirthDate
import com.example.baism_cw_03.validation.Validators.validateEmail
import com.example.baism_cw_03.validation.Validators.validateName
import com.example.baism_cw_03.validation.Validators.validatePhone
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle

data class PersonForm(
    val firstName: String = "",
    val lastName: String = "",
    val phone: String,
    val email: String = "",
    val birthDate: String = "",
    val address: String = ""
)

data class PersonErrors(
    val firstName: String? = null,
    val lastName: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val birthDate: String? = null,
    val address: String? = null
) {
    val hasErrors: Boolean
        get() = listOfNotNull(firstName, lastName, phone, email, birthDate, address).isNotEmpty()
}

object Validators {
    val DATE_FORMAT: DateTimeFormatter =
        DateTimeFormatter.ofPattern("dd.MM.uuuu").withResolverStyle(ResolverStyle.STRICT)

    private val EMAIL_REGEX =
        Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$")

    private val NAME_REGEX = Regex("^\\p{L}[\\p{L} '-]*$")
    private val PHONE_CHARS_REGEX = Regex("^\\+?[0-9 -]+$")

    fun validateName(value: String, label: String): String? {
        val v = value.trim()
        return when {
            v.isEmpty() -> "Pole $label jest wymagane"
            v.length > 50 -> "Pole $label jest za długie (max 50 znaków)"
            !NAME_REGEX.matches(v) -> "Pole $label zawiera niedozwolone znaki"
            else -> null
        }
    }

    fun validateBirthDate(value: String, today: LocalDate = LocalDate.now()): String? {
        val v = value.trim()

        if (v.isEmpty()) return "Data urodzenia jest wymagana"

        val date = try {
            LocalDate.parse(v, DATE_FORMAT)
        } catch (e: DateTimeParseException) {
            return "Zły format daty. Poprawny: dd.mm.yyyy"
        }
        return when {
            date.isAfter(today) -> "Data urodzenia nie może być z przyszłości"
            date.isBefore(today.minusYears(150)) -> "Data urodzenia jest nierealna"
            else -> null
        }
    }

    fun validatePhone(value: String): String? {
        val v = value.trim()

        if (v.isEmpty()) return "Numer telefonu jest wymaganym polem"

        if (!PHONE_CHARS_REGEX.matches(v)) return "Numer telefonu może zawierać tylko liczby"

        val digits = v.count { it.isDigit() }
        return if (digits in 9..15) null else "Numer telefonu musi mieć od 9 do 15 cyfr"
    }

    fun validateEmail(value: String): String? {
        val v = value.trim()
        return when {
            v.isEmpty() -> "Pole e-mail jest wymagane"
            !EMAIL_REGEX.matches(v) -> "Podaj poprawny adres e-mail"
            else -> null
        }
    }

    fun validateAddress(value: String): String? {
        val v = value.trim()
        return when {
            v.isEmpty() -> "Adres jest wymagany"
            v.length > 200 -> "Adres jest za długi (max 200 znaków)"
            else -> null
        }
    }
    fun validate(form: PersonForm, today: LocalDate = LocalDate.now()): PersonErrors = PersonErrors(
        firstName = validateName(form.firstName, "Imię"),
        lastName = validateName(form.lastName, "Nazwisko"),
        phone = validatePhone(form.phone),
        email = validateEmail(form.email),
        birthDate = validateBirthDate(form.birthDate, today),
        address = validateAddress(form.address),
    )
}
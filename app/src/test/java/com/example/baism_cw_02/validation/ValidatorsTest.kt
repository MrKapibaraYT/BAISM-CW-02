package com.example.baism_cw_02.validation

import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class ValidatorsTest {

    private val today = LocalDate.now()
    @Test
    fun validateName() {
        assertNull(Validators.validateName("Jan", "Imię"))
        assertNull(Validators.validateName("Żaneta", "Imię"))
        assertNull(Validators.validateName("Kowalski-Nowak", "Nazwisko"))
        assertNotNull(Validators.validateName("", "Imię"))
        assertNotNull(Validators.validateName("   ", "Imię"))
        assertNotNull(Validators.validateName("Jan2", "Imię"))
    }

    @Test
    fun validateBirthDate() {
        assertNull(Validators.validateBirthDate("10.09.2004", today))
        assertNull(Validators.validateBirthDate("29.02.2000", today))
        assertNull(Validators.validateBirthDate("25.11.2026", today))
        assertNotNull(Validators.validateBirthDate("31.02.2000", today))
        assertNotNull(Validators.validateBirthDate("2004-09-10", today))
        assertNotNull(Validators.validateBirthDate("26.11.2026", today))
        assertNotNull(Validators.validateBirthDate("", today))
    }

    @Test
    fun validatePhone() {
        assertNull(Validators.validatePhone("123456789"))
        assertNull(Validators.validatePhone("+48 123 423 212"))
        assertNull(Validators.validatePhone("123-456-789"))
        assertNotNull(Validators.validatePhone("12345"))
        assertNotNull(Validators.validatePhone("12345abc9"))
        assertNotNull(Validators.validatePhone(""))
    }

    @Test
    fun validateEmail() {
        assertNull(Validators.validateEmail("jan.kowalski@example.com"))
        assertNotNull(Validators.validateEmail("jan.kowalski"))
        assertNotNull(Validators.validateEmail("jan@"))
        assertNotNull(Validators.validateEmail("@example.com"))
        assertNotNull(Validators.validateEmail("jan@example"))
        assertNotNull(Validators.validateEmail(""))
    }

    @Test
    fun validateAddress() {
        assertNull(Validators.validateAddress("ul. Długa 5/3, 00-001 Warszawa"))
        assertNotNull(Validators.validateAddress(""))
        assertNotNull(Validators.validateAddress("a".repeat(201)))
    }

    @Test
    fun validate() {
        val valid = PersonForm(
            firstName = "Jan", lastName = "Kowalski", phone = "123456789",
            email = "jan@example.com", birthDate = "15.03.1990", address = "Warszawa",
        )
        assertFalse(Validators.validate(valid, today).hasErrors)

        val empty = Validators.validate(PersonForm(
            firstName = "",
            lastName = "",
            phone = "",
            email = "",
            birthDate = "",
            address = "",
        ), today)
        assertTrue(empty.hasErrors)
        assertNotNull(empty.firstName)
        assertNotNull(empty.email)

        val oneBad = Validators.validate(valid.copy(email = "zly"), today)
        assertNotNull(oneBad.email)
        assertNull(oneBad.firstName)
        assertNull(oneBad.phone)
    }

}
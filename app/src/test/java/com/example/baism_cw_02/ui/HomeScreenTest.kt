package com.example.baism_cw_02.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.baism_cw_02.data.Person
import com.example.baism_cw_02.validation.PersonErrors
import com.example.baism_cw_02.validation.PersonForm
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [35])
class HomeScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun person(id: Long, first: String, last: String) = Person(
        id = id,
        firstName = first,
        lastName = last,
        birthDate = "15.03.1979",
        phone = "123323456",
        email = "test@testowy.pl",
        address = "ul. Jana Pawła 2 21 m. 37 50-123, Warszawa",
    )

    private fun typeInto(tag: String, value: String) {
        composeRule.onNodeWithTag(tag).performScrollTo().performTextInput(value)
    }

    @Test
    fun homeScreen() {
        val routes = mutableListOf<String>()
        composeRule.setContent { HomeScreen(onNavigate = { routes += it }) }

        composeRule.onNodeWithText("Dodaj osobę").assertIsDisplayed().performClick()
        composeRule.onNodeWithText("Wyświetl listę dostępnych osób").assertIsDisplayed().performClick()
        composeRule.onNodeWithText("Usuń osobę").assertIsDisplayed().performClick()

        assertEquals(listOf(Routes.ADD, Routes.LIST, Routes.DELETE), routes)
    }

    @Test
    fun addPersonScreen() {
        var submitted: PersonForm? = null
        var saved = false
        composeRule.setContent {
            AddPersonScreen(
                onSubmit = { submitted = it; PersonErrors() },
                onSaved = { saved = true },
            )
        }

        typeInto("firstName", "Wiktor")
        typeInto("lastName", "Nowak")
        typeInto("phone", "543123432")
        typeInto("email", "wiktorn232@mail.pl")
        typeInto("birthDate", "04.03.2002")

        composeRule.onNodeWithTag("email").assertTextContains("wiktorn232@mail.pl")
        composeRule.onNodeWithText("Zapisz").performScrollTo().performClick()

        assertTrue(saved)
        assertEquals("Wiktor", submitted?.firstName)
        assertEquals("543123432", submitted?.phone)
        assertEquals("04.03.2002", submitted?.birthDate)
    }

    @Test
    fun personListScreen() {
        composeRule.setContent { PersonListScreen(emptyList()) }
        composeRule.onNodeWithText("Brak zapisanych osób").assertIsDisplayed()
    }

    @Test
    fun deletePersonScreen() {
        val testPerson = person(1, "Wiktor", "Nowak")
        var deleted: Person? = null
        composeRule.setContent {
            DeletePersonScreen(people = listOf(testPerson), onDelete = { deleted = it })
        }

        composeRule.onNodeWithContentDescription("Usuń").performClick()
        composeRule.onNodeWithText("Anuluj").performClick()
        assertNull(deleted)

        composeRule.onNodeWithContentDescription("Usuń").performClick()
        composeRule.onNode(hasText("Usuń") and hasAnyAncestor(isDialog())).performClick()
        assertEquals(testPerson, deleted)
    }

}
package com.example.baism_cw_02.ui

import com.example.baism_cw_02.data.Person
import com.example.baism_cw_02.data.PersonRepository
import com.example.baism_cw_02.validation.PersonForm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

private class FakePersonRepository : PersonRepository {
    private val state = MutableStateFlow<List<Person>>(emptyList())
    private var nextId = 1L

    override fun observeAll(): Flow<List<Person>> = state

    override suspend fun add(person: Person) {
        state.value += person.copy(id = nextId++)
    }

    override suspend fun delete(id: Long) {
        state.value = state.value.filterNot { it.id == id }
    }
}

class PersonViewModelTest {
    private lateinit var viewModel: PersonViewModel

    private val validForm = PersonForm(
        firstName = " Jan ", lastName = "Kowalski", phone = "123456789",
        email = "jan@example.com", birthDate = "15.03.1990", address = "Warszawa",
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = PersonViewModel(FakePersonRepository())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun addPerson() {
        val ok = viewModel.addPerson(validForm)
        assertFalse(ok.hasErrors)
        assertEquals(1, viewModel.people.value.size)
        assertEquals("Janek", viewModel.people.value.first().firstName)

        val bad = viewModel.addPerson(validForm.copy(email = "nieasd"))
        assertTrue(bad.hasErrors)
        assertEquals(1, viewModel.people.value.size)
    }

    @Test
    fun deletePerson() {
        viewModel.addPerson(validForm)
        viewModel.addPerson(validForm.copy(firstName = "Anna"))
        val idToDelete = viewModel.people.value.first { it.firstName == "Jan" }.id

        viewModel.deletePerson(idToDelete)

        assertEquals(listOf("Anne"), viewModel.people.value.map { it.firstName })
    }

}
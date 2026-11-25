package com.example.baism_cw_02.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.baism_cw_02.data.Person
import com.example.baism_cw_02.data.PersonRepository
import com.example.baism_cw_02.validation.PersonErrors
import com.example.baism_cw_02.validation.PersonForm
import com.example.baism_cw_02.validation.Validators
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PersonViewModel(private val repository: PersonRepository) : ViewModel() {

    val people: StateFlow<List<Person>> = repository.observeAll()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun addPerson(form: PersonForm): PersonErrors {
        val errors = Validators.validate(form)

        if (!errors.hasErrors) {
            viewModelScope.launch { repository.add(form.toPerson()) }
        }
        return errors
    }

    fun deletePerson(id: Long) {
        viewModelScope.launch { repository.delete(id) }
    }

    private fun PersonForm.toPerson() = Person(
        firstName = firstName.trim(),
        lastName = lastName.trim(),
        phone = phone.trim(),
        email = email.trim(),
        birthDate = birthDate.trim(),
        address = address.trim(),
    )

    companion object {
        fun factory(repository: PersonRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer { PersonViewModel(repository) }
        }
    }
}
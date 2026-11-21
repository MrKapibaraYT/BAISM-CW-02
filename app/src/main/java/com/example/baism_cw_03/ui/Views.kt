package com.example.baism_cw_03.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.baism_cw_03.data.Person
import com.example.baism_cw_03.utils.EmptyState
import com.example.baism_cw_03.utils.FormField
import com.example.baism_cw_03.utils.PersonCard
import com.example.baism_cw_03.validation.PersonErrors
import com.example.baism_cw_03.validation.PersonForm

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Dane osobowe", style = MaterialTheme.typography.headlineMedium)
        Button(onClick = { onNavigate(Routes.ADD) }, modifier = Modifier.fillMaxWidth()) {
            Text("Dodaj osobę")
        }
        Button(onClick = { onNavigate(Routes.LIST) }, modifier = Modifier.fillMaxWidth()) {
            Text("Wyświetl listę dostępnych osób")
        }
        Button(onClick = { onNavigate(Routes.DELETE) }, modifier = Modifier.fillMaxWidth() ) {
            Text("Usuń osobę")
        }
    }
}

@Composable
fun AddPersonScreen(
    onSubmit: (PersonForm) -> PersonErrors,
    onSaved: () -> Unit
) {
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var birthDate by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var errors by remember { mutableStateOf(PersonErrors()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        FormField("Imię", firstName, { firstName = it }, errors.firstName)
        FormField("Nazwisko", lastName, { lastName = it }, errors.lastName)
        FormField("Nr telefonu (np. +48 123 423 212", phone, {phone = it }, errors.phone)
        FormField("Adres e-mail", email, { email = it }, errors.email)
        FormField("Data urodzenia (DD.MM.YYYY)", birthDate, { birthDate = it }, errors.birthDate)
        FormField("Adres", address, { address = it }, errors.address)

        Button(
            onClick = {
                val result = onSubmit(PersonForm(firstName,lastName,phone,email,birthDate,address))
                errors = result
                if (!result.hasErrors) {
                    firstName = ""; lastName = ""; phone = "";
                    email = ""; birthDate = ""; address = ""
                    onSaved()
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) { Text("Zapisz") }
    }
}

@Composable
fun PersonListScreen(people: List<Person>) {
    if (people.isEmpty()) {
        EmptyState()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp),
        ) {
            items(people, key = { it.id }) { person -> PersonCard(person) }
        }
    }
}
@Composable
fun DeletePersonScreen(people: List<Person>, onDelete: (Person) -> Unit) {
    var toDelete by remember { mutableStateOf<Person?>(null) }

    if (people.isEmpty()) {
        EmptyState()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp),
        ) {
            items(people, key = { it.id }) { person ->
                PersonCard(person) {
                    IconButton(onClick = { toDelete = person }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Usuń")
                    }
                }
            }
        }
    }

    toDelete?.let { person ->
        AlertDialog(
            onDismissRequest = { toDelete = null },
            title = { Text("Usunąć osobę?") },
            text = { Text("${person.firstName} ${person.lastName} zostanie trwale usunięta.") },
            confirmButton = {
                TextButton(onClick = { toDelete = null }) { Text("Usuń") }
            },
            dismissButton = {
                TextButton(onClick = { toDelete = null }) { Text("Anuluj") }
            },
        )
    }
}
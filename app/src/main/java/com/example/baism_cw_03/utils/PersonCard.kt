package com.example.baism_cw_03.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.baism_cw_03.data.Person

@Composable
fun PersonCard(person: Person, trailing: @Composable (() -> Unit)? = null) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("${person.lastName} ${person.firstName} ", style = MaterialTheme.typography.titleLargeEmphasized)
                Text("Tel.: ${person.phone}")
                Text("E-mail: ${person.email}")
                Text("Data urodzenia: ${person.birthDate}")
                Text("Adres: ${person.address}")
            }
            trailing?.invoke()
        }
    }
}
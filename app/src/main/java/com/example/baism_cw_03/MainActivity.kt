package com.example.baism_cw_03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.baism_cw_03.data.AppDatabase
import com.example.baism_cw_03.data.RoomPersonRepository
import com.example.baism_cw_03.ui.PersonViewModel
import com.example.baism_cw_03.ui.theme.BAiSMCW03Theme

class MainActivity : ComponentActivity() {

    private val viewModel: PersonViewModel by viewModels {
        PersonViewModel.factory(
            RoomPersonRepository(AppDatabase.get(applicationContext).personDao)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme { AppNav(viewModel) }
        }
    }
}
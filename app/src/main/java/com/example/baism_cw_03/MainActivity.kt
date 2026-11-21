package com.example.baism_cw_03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import com.example.baism_cw_03.data.AppDatabase
import com.example.baism_cw_03.data.RoomPersonRepository
import com.example.baism_cw_03.ui.AppNav
import com.example.baism_cw_03.ui.PersonViewModel

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
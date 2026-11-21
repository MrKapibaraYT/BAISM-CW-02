package com.example.baism_cw_03.data

import android.location.Address
import androidx.room3.Entity
import androidx.room3.PrimaryKey


@Entity(tableName = "people")
data class Person(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val birthDate: String,
    val address: String
)
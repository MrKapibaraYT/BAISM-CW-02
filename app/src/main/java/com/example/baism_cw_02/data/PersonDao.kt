package com.example.baism_cw_02.data

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface PersonDao {
    @Query("SELECT * FROM people ORDER BY lastName COLLATE NOCASE, firstName COLLATE NOCASE")
    fun observeAll(): Flow<List<Person>>
    @Insert
    suspend fun insert(person: Person): Long

    @Query("DELETE FROM people WHERE id = :id")
    suspend fun deleteById(id: Long)
}
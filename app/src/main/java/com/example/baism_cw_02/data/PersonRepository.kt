package com.example.baism_cw_02.data

import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    fun observeAll(): Flow<List<Person>>
    suspend fun add(person: Person)
    suspend fun delete(id: Long)
}

class RoomPersonRepository(private val dao: PersonDao) : PersonRepository {
    override fun observeAll(): Flow<List<Person>> = dao.observeAll()

    override suspend fun add(person: Person) { dao.insert(person) }

    override suspend fun delete(id: Long) { dao.deleteById(id) }

}
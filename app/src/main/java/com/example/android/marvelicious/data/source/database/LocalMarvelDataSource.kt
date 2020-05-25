package com.example.android.marvelicious.data.source.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.DataSource
import com.example.android.marvelicious.data.source.MarvelDataSource
import com.example.android.marvelicious.domain.Models
import com.example.android.marvelicious.domain.asDatabaseModel
import kotlinx.coroutines.*

class LocalMarvelDataSource(
    private val charactersDao: CharactersDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MarvelDataSource {

    override suspend fun <T> saveObjects(characters: List<T>) = withContext(ioDispatcher) {
        characters as List<Models.Character>
        charactersDao.insertAll(characters.asDatabaseModel()!!)
    }

    override suspend fun <T> getObjects(offset: Int, limit: Int): List<T> {
        throw NotImplementedError()
    }

    override fun <T> getObjectsDataSource(): DataSource.Factory<Int, T> {
        return charactersDao.getAllCharacters().map {
            it.asDomainModel() as T
        }
    }

    override fun getTotalObjectsCount(): Int {
        return charactersDao.getTotalCharactersCount()
    }

    override fun deleteAllObjects() {
        CoroutineScope(ioDispatcher).launch {
            charactersDao.deleteAllCharacters()
        }
    }

    override suspend fun <T> saveObject(character: T) {
        CoroutineScope(ioDispatcher).launch {
            character as Models.Character
            charactersDao.insert(character.asDatabaseModel())
        }
    }

    override suspend fun <T> getObject(id: Int): T {
        throw NotImplementedError()
    }

    override fun <T> getObjectDataSource(id: Int): LiveData<T> {
        val fromDb = charactersDao.getMarvelCharacter(id)

        return Transformations.map(fromDb) {
            it.asDomainModel() as T
        }
    }
}
package com.example.android.marvelicious.data.source.database

import androidx.paging.DataSource
import com.example.android.marvelicious.data.source.MarvelDataSource
import com.example.android.marvelicious.domain.Models
import com.example.android.marvelicious.domain.asDatabaseModel
import kotlinx.coroutines.*

class LocalMarvelDataSource(
    private val charactersDao: CharactersDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MarvelDataSource {

    fun getDataForNow(): DataSource.Factory<Int, Models.Character> {
        return charactersDao.getAllCharacters().map {
            it.asDomainModel()
        }
    }

    override suspend fun <T> saveObjects(characters: List<T>) = withContext(ioDispatcher) {
        characters as List<Models.Character>
        charactersDao.insertAll(characters.asDatabaseModel()!!)
    }

    override suspend fun <T> getObjects(offset: Int, limit: Int): List<T> {
        throw NotImplementedError()
    }

    override fun <T> getObjectDataSource(): DataSource.Factory<Int, T> {
        return charactersDao.getAllCharacters().map {
            it.asDomainModel() as T
        }
    }

    override fun getTotalObjectsCount(): Int {
        return charactersDao.getTotalCharactersCount()
    }

    override fun deleteAllObjects() {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            charactersDao.deleteAllCharacters()
        }
    }
}
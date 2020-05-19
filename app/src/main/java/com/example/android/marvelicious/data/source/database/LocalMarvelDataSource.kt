package com.example.android.marvelicious.data.source.database

import androidx.paging.DataSource
import com.example.android.marvelicious.data.source.MarvelDataSource
import com.example.android.marvelicious.domain.Models
import com.example.android.marvelicious.domain.asDatabaseModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
        val charactersToStore = characters as List<Models.Character>
        charactersDao.insertAll(characters.asDatabaseModel()!!)
    }

    override suspend fun <T> getObjects(): List<T> {
        throw NotImplementedError()
    }

    override fun <T> getObjectDataSource(): DataSource.Factory<Int, T> {
        return charactersDao.getAllCharacters().map {
            it.asDomainModel() as T
        }
    }
}
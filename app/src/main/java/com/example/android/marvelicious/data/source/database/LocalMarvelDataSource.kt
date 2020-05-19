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

    override suspend fun saveCharacters(characters: List<Models.Character>) =
        withContext(ioDispatcher) {
            charactersDao.insertAll(characters.asDatabaseModel()!!)
        }

    override suspend fun getCharacters(): List<Models.Character> = throw NotImplementedError()
}
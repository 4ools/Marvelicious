package com.example.android.marvelicious.data.source.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.marvelicious.data.source.DataSource
import com.example.android.marvelicious.domain.Models
import com.example.android.marvelicious.domain.asDatabaseModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalDataSource(
    private val charactersDao: CharactersDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DataSource {

    override fun observeCharacters(): LiveData<List<Models.Character>> {
        return Transformations.map(charactersDao.getAllCharacters()) {
            it.asDomainModel()
        }
    }

    override suspend fun refreshCharacters() {
        throw NotImplementedError()
    }

    override suspend fun saveCharacters(characters: List<Models.Character>) = withContext(ioDispatcher) {
        charactersDao.insertAll(characters.asDatabaseModel()!!)
    }

    override suspend fun getCharacters(): List<Models.Character> = withContext(ioDispatcher) {
        return@withContext charactersDao.getChars().asDomainModel()
    }
}
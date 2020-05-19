package com.example.android.marvelicious.data.source.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.marvelicious.data.Result
import com.example.android.marvelicious.data.source.DataSource
import com.example.android.marvelicious.domain.Models
import com.example.android.marvelicious.domain.asDatabaseModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class LocalDataSource(
    private val charactersDao: CharactersDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DataSource {

    override fun observeCharacters(): LiveData<Result<List<Models.Character>>> {
        return Transformations.map(charactersDao.getAllCharacters()) {
            Result.Success(it.asDomainModel())
        }
    }

    override suspend fun refreshCharacters() {
        throw NotImplementedError()
    }

    override suspend fun saveCharacters(characters: List<Models.Character>) =
        withContext(ioDispatcher) {
            charactersDao.insertAll(characters.asDatabaseModel()!!)
        }

    override suspend fun getCharacters(): Result<List<Models.Character>> =
        withContext(ioDispatcher) {
            return@withContext try {
                Result.Success(charactersDao.getChars().asDomainModel())
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
}
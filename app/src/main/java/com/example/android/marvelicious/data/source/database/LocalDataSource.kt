package com.example.android.marvelicious.data.source.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.marvelicious.data.source.DataSource
import com.example.android.marvelicious.domain.Models
import com.example.android.marvelicious.domain.asDatabaseModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class LocalDataSource(
    private val charactersDao: CharactersDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DataSource {

    override fun getCharacters(): LiveData<List<Models.Character>> {
        return Transformations.map(charactersDao.getAllCharacters()) {
            it.asDomainModel()
        }
    }

    override fun insertAll(characters: List<Models.Character>) {
        charactersDao.insertAll(characters.asDatabaseModel()!!)
    }
}
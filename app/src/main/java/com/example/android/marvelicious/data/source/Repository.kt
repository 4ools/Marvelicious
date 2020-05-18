package com.example.android.marvelicious.data.source

import com.example.android.marvelicious.data.source.database.LocalDataSource
import com.example.android.marvelicious.data.source.network.MarveliciousService
import com.example.android.marvelicious.data.source.network.asCharacterDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val localDataSource: LocalDataSource) {
    private val marvelApi by lazy {
        MarveliciousService.create()
    }

    var characters = localDataSource.getCharacters()

    suspend fun refreshCharacters() {
        withContext(Dispatchers.IO) {
            val returnedCharacters = marvelApi.getAllCharactersAsync(
                limit = 50,
                offset = 0
            ).await()
            localDataSource.insertAll(returnedCharacters.asCharacterDomainModel()!!)
        }
    }
}
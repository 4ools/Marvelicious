package com.example.android.marvelicious.data.source.network

import androidx.paging.DataSource
import com.example.android.marvelicious.data.source.MarvelDataSource
import com.example.android.marvelicious.domain.Models

class RemoteMarvelDataSource : MarvelDataSource {
    private val marvelApi by lazy {
        MarveliciousService.create()
    }

    override suspend fun <T> saveObjects(characters: List<T>) {
        throw NotImplementedError()
    }

    override suspend fun <T> getObjects(offset: Int, limit: Int): List<T> {
        val returnedCharacters = marvelApi.getAllCharactersAsync(
            limit = limit ,
            offset = offset
        ).await()

        return returnedCharacters.asCharacterDomainModel()!! as List<T>
    }

    override fun <T> getObjectDataSource(): DataSource.Factory<Int, T> {
        throw NotImplementedError()
    }

    override fun getTotalObjectsCount(): Int {
        throw NotImplementedError()
    }
}
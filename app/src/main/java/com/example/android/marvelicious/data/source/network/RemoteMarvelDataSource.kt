package com.example.android.marvelicious.data.source.network

import com.example.android.marvelicious.data.source.MarvelDataSource
import com.example.android.marvelicious.domain.Models

class RemoteMarvelDataSource : MarvelDataSource {
    private val marvelApi by lazy {
        MarveliciousService.create()
    }

    override suspend fun saveCharacters(characters: List<Models.Character>) {
        throw NotImplementedError()
    }

    override suspend fun getCharacters(): List<Models.Character> {
        val returnedCharacters = marvelApi.getAllCharactersAsync(
            limit = 50,
            offset = 0
        ).await()

        return returnedCharacters.asCharacterDomainModel()!!
    }
}
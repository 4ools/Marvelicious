package com.example.android.marvelicious.data.source.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.marvelicious.data.source.DataSource
import com.example.android.marvelicious.domain.Models

class RemoteDataSource : DataSource {
    private val marvelApi by lazy {
        MarveliciousService.create()
    }

    private val observableCharacters = MutableLiveData<List<Models.Character>>()

    override fun observeCharacters(): LiveData<List<Models.Character>> {
        return observableCharacters
    }

    override suspend fun refreshCharacters() {
        observableCharacters.value = getCharacters()
    }

    override suspend fun saveCharacters(characters: List<Models.Character>) {
        throw NotImplementedError()
    }

    override suspend fun getCharacters(): List<Models.Character> {
        // Simulate network by delaying the execution.
        val returnedCharacters = marvelApi.getAllCharactersAsync(
            limit = 50,
            offset = 0
        ).await()

        return returnedCharacters.asCharacterDomainModel()!!
    }
}
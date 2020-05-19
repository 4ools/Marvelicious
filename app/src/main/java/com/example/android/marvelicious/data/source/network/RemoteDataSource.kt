package com.example.android.marvelicious.data.source.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.marvelicious.data.Result
import com.example.android.marvelicious.data.source.DataSource
import com.example.android.marvelicious.domain.Models
import timber.log.Timber

class RemoteDataSource : DataSource {
    private val marvelApi by lazy {
        MarveliciousService.create()
    }

    private val observableCharacters = MutableLiveData<Result<List<Models.Character>>>()

    override fun observeCharacters(): LiveData<Result<List<Models.Character>>> {
        return observableCharacters
    }

    override suspend fun refreshCharacters() {
        observableCharacters.value = getCharacters()
    }

    override suspend fun saveCharacters(characters: List<Models.Character>) {
        throw NotImplementedError()
    }

    override suspend fun getCharacters(): Result<List<Models.Character>> {
        return try {
            val returnedCharacters = marvelApi.getAllCharactersAsync(
                limit = 20,
                offset = 0
            ).await()
            Result.Success(returnedCharacters.asCharacterDomainModel()!!)
        } catch (e: Exception) {
            Timber.e("caught an error $e")
            Result.Error(e)
        }
    }
}
package com.example.android.marvelicious.repository

import androidx.lifecycle.MutableLiveData
import com.example.android.marvelicious.domain.Models
import com.example.android.marvelicious.network.MarveliciousService
import com.example.android.marvelicious.network.asCharacterDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class Repository {
    private val marvelApi by lazy {
        MarveliciousService.create()
    }

    var characters = MutableLiveData<List<Models.Character>>()

    suspend fun refreshCharacters() {
        withContext(Dispatchers.IO) {
            Timber.i("refresh characters called")
            val returnedCharacters = marvelApi.getAllCharactersAsync(
                limit = 50,
                offset = 0
            ).await()
            Timber.d("the size returned is ${returnedCharacters.data?.results?.size}")
            characters.postValue(returnedCharacters.asCharacterDomainModel())
        }
    }
}
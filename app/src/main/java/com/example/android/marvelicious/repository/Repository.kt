package com.example.android.marvelicious.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.marvelicious.network.DataTransferObject
import com.example.android.marvelicious.network.MarveliciousService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class Repository {
    private val marvelApi by lazy {
        MarveliciousService.create()
    }

    var characters = MutableLiveData<DataTransferObject.CharacterDataWrapper>()

    suspend fun refreshCharacters() {
        withContext(Dispatchers.IO) {
            Timber.i("refresh characters called")
            val returnedCharacters = marvelApi.getAllCharactersAsync(
                limit = 50,
                offset = 0
            ).await()
            Timber.d("the size returned is ${returnedCharacters.data?.results?.size}")
            characters.postValue(returnedCharacters)
        }
    }
}
package com.example.android.marvelicious.repository

import com.example.android.marvelicious.network.MarveliciousService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class CharacterRepository {
    private val marvelApi by lazy {
        MarveliciousService.create()
    }

    suspend fun refreshCharacters() {
        withContext(Dispatchers.IO) {
            Timber.i("refresh characters called")
            val characters = marvelApi.getAllCharacters(
                limit = 50,
                offset = 0
            ).await()
            Timber.d("the size returned is ${characters.data?.results?.size}")
        }
    }
}
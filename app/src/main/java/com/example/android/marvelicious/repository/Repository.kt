package com.example.android.marvelicious.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.marvelicious.database.MarveliciousDatabase
import com.example.android.marvelicious.database.asDomainModel
import com.example.android.marvelicious.domain.Models
import com.example.android.marvelicious.network.MarveliciousService
import com.example.android.marvelicious.network.asCharacterDomainModel
import com.example.android.marvelicious.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class Repository(private val database: MarveliciousDatabase) {
    private val marvelApi by lazy {
        MarveliciousService.create()
    }

    var characters = Transformations.map(database.videoDao.getAllCharacters()) {
        it.asDomainModel()
    }

    suspend fun refreshCharacters() {
        withContext(Dispatchers.IO) {
            Timber.i("refresh characters called")
            val returnedCharacters = marvelApi.getAllCharactersAsync(
                limit = 50,
                offset = 0
            ).await()
            Timber.d("the size returned is ${returnedCharacters.data?.results?.size}")
            database.videoDao.insertAll(returnedCharacters.asDatabaseModel()!!)
        }
    }
}
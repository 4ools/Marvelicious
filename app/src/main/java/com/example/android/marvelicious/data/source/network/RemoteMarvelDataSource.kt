package com.example.android.marvelicious.data.source.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.android.marvelicious.data.source.MarvelDataSource
import com.example.android.marvelicious.domain.Models
import timber.log.Timber

class RemoteMarvelDataSource : MarvelDataSource {
    private val marvelApi by lazy {
        MarveliciousService.create()
    }

    override suspend fun <T> saveObjects(characters: List<T>) {
        throw NotImplementedError()
    }

    override suspend fun <T> getObjects(offset: Int, limit: Int): List<T> {
        val returnedCharacters = marvelApi.getAllCharactersAsync(
            limit = limit,
            offset = offset
        ).await()

        return returnedCharacters.asCharacterDomainModel()!! as List<T>
    }

    override fun <T> getObjectsDataSource(): DataSource.Factory<Int, T> {
        throw NotImplementedError()
    }

    override fun getTotalObjectsCount(): Int {
        throw NotImplementedError()
    }

    override fun deleteAllObjects() {
        throw NotImplementedError()
    }

    override suspend fun <T> saveObject(objectToSave: T) {
        throw NotImplementedError()
    }

    override suspend fun <T> getObject(id: Int): T {
        val returnedCharacter = marvelApi.getCharacter(id).await()
        return returnedCharacter.asCharacterDomainModel() as T
    }

    override fun <T> getObjectDataSource(id: Int): LiveData<T> {
        throw NotImplementedError()
    }
}
package com.example.android.marvelicious.data.source.database

import androidx.lifecycle.MutableLiveData
import com.example.android.marvelicious.data.NetworkState
import com.example.android.marvelicious.data.SingleResult
import com.example.android.marvelicious.data.source.MarvelDataSource
import com.example.android.marvelicious.domain.Models
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

object CharacterDetailCallback {
    val networkState = MutableLiveData<NetworkState>()
    fun getCharacterDetail(
        remoteDataSource: MarvelDataSource,
        localDataSource: MarvelDataSource,
        forceUpdate: Boolean,
        id: Int
    ): SingleResult {

        networkState.postValue(NetworkState.LOADING)

        if (forceUpdate) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val r = remoteDataSource.getObject<List<Models.Character>>(id)
                    localDataSource.saveObject(
                        r.first()
                    )
                    networkState.postValue(NetworkState.LOADED)

                } catch (e: Exception) {
                    Timber.e("caught in repo $e")
                    networkState.postValue(NetworkState.error(e.message))
                }
            }
        }

        val character = localDataSource.getObjectDataSource<Models.Character>(id)
        if (!forceUpdate) {
            networkState.postValue(NetworkState.LOADED)
        }

        return SingleResult(character,
            networkState
        ) {
            getCharacterDetail(
                remoteDataSource,
                localDataSource,
                true,
                id
            )
        }
    }
}
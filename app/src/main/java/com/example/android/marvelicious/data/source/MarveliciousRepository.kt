package com.example.android.marvelicious.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.example.android.marvelicious.data.NetworkState
import com.example.android.marvelicious.data.Result
import com.example.android.marvelicious.data.SingleResult
import com.example.android.marvelicious.data.source.database.MarveliciousCharacterBoundaryCallback
import com.example.android.marvelicious.domain.Models
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber


class MarveliciousRepository(
    private val remoteDataSource: MarvelDataSource,
    private val localDataSource: MarvelDataSource
) : Repository {
    companion object {
        const val PAGE_SIZE = 20
    }

    override suspend fun getCharacters(): Result {
        throw NotImplementedError()
    }

    override fun refreshCharacters(forceUpdate: Boolean):
            Result {
        if (forceUpdate) {
            localDataSource.deleteAllObjects()
        }
        val dataSourceFactory = localDataSource.getObjectsDataSource<Models.Character>()

        val boundaryCallback = MarveliciousCharacterBoundaryCallback(
            remoteDataSource,
            localDataSource
        )

        val data = LivePagedListBuilder(dataSourceFactory, PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()

        val networkState = boundaryCallback.networkState

        return Result(data, networkState) {
            refreshCharacters(true)
        }
    }

    override suspend fun getCharacter(id: Int): SingleResult {
        throw NotImplementedError()
    }

    override fun refreshCharacter(forceUpdate: Boolean, id: Int): SingleResult {
        val networkState = MutableLiveData<NetworkState>()

//        if (forceUpdate) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                networkState.postValue(NetworkState.LOADING)

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
//        }
        networkState.postValue(NetworkState.LOADING)
        val character = localDataSource.getObjectDataSource<Models.Character>(id)
        networkState.postValue(NetworkState.LOADED)
        return SingleResult(character, networkState) {
            refreshCharacter(true, id)
        }
//        return try {
//            networkState.postValue(NetworkState.LOADING)
//            Timber.d("its loading $id")
//            val character =
//                networkState.postValue(NetworkState.LOADED)
//
//        } catch (e: Exception) {
//            Timber.e(e)
//            networkState.postValue(NetworkState.error(e.message))
//            SingleResult(, networkState) {
//                refreshCharacter(true, id)
//            }
//        }
    }
}
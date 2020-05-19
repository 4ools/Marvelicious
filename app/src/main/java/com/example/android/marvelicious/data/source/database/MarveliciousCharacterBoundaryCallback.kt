package com.example.android.marvelicious.data.source.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.android.marvelicious.data.NetworkState
import com.example.android.marvelicious.data.source.MarvelDataSource
import com.example.android.marvelicious.domain.Models
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MarveliciousCharacterBoundaryCallback(
    private val remoteDataSource: MarvelDataSource,
    private val localDataSource: MarvelDataSource
) : PagedList.BoundaryCallback<Models.Character>() {
    private val viewModelScope = CoroutineScope(Dispatchers.IO)

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    companion object {
        const val NETWORK_PAGE_SIZE = 50
    }

    override fun onZeroItemsLoaded() {
        Timber.d("onZeroItemsLoaded")
        viewModelScope.launch {
            val offset = localDataSource.getTotalObjectsCount()
            requestAndSaveData(offset)
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Models.Character) {
        Timber.d("onItemAtEndLoaded with ${itemAtEnd.name}")
        viewModelScope.launch {
            val offset = localDataSource.getTotalObjectsCount()
            requestAndSaveData(offset)
        }
    }

    private fun requestAndSaveData(offSet: Int, limit: Int = NETWORK_PAGE_SIZE) {
        Timber.d("requestAndSaveData with $offSet and $limit")
        viewModelScope.launch {
            try {
                _networkState.postValue(NetworkState.LOADING)
                localDataSource.saveObjects<Models.Character>(
                    remoteDataSource.getObjects(
                        offSet, limit
                    )
                )
                _networkState.postValue(NetworkState.LOADED)
            } catch (e: Exception) {
                Timber.e("caught in repo $e")
                _networkState.postValue(NetworkState.error(e.message))
            }
        }
    }
}
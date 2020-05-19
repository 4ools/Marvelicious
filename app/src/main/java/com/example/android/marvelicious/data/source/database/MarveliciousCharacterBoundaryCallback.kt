package com.example.android.marvelicious.data.source.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.android.marvelicious.data.NetworkState
import com.example.android.marvelicious.data.source.network.RemoteMarvelDataSource
import com.example.android.marvelicious.domain.Models
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MarveliciousCharacterBoundaryCallback(
    private val remoteDataSource: RemoteMarvelDataSource,
    private val localDataSource: LocalMarvelDataSource
) : PagedList.BoundaryCallback<Models.Character>() {
    private val viewModelScope = CoroutineScope(Dispatchers.Main)
    private var isRequestInProgress = false


    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    override fun onZeroItemsLoaded() {
        requestAndSaveData()
    }

    private fun requestAndSaveData() {
        if (isRequestInProgress) return
        viewModelScope.launch {
            isRequestInProgress = true
            try {
                _networkState.postValue(NetworkState.LOADING)
                localDataSource.saveObjects<Models.Character>(remoteDataSource.getObjects())
                _networkState.postValue(NetworkState.LOADED)
            } catch (e: Exception) {
                Timber.e("caught in repo $e")
                _networkState.postValue(NetworkState.error(e.message))
            }
        }
    }
}
package com.example.android.marvelicious.ui

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.example.android.marvelicious.data.NetworkState
import com.example.android.marvelicious.data.Result
import com.example.android.marvelicious.data.source.MarveliciousRepository
import com.example.android.marvelicious.data.source.database.LocalMarvelDataSource
import com.example.android.marvelicious.data.source.database.getDatabase
import com.example.android.marvelicious.data.source.network.RemoteMarvelDataSource
import com.example.android.marvelicious.domain.Models
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class CharactersViewModel(application: Application) : AndroidViewModel(application) {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val repository =
        MarveliciousRepository(
            RemoteMarvelDataSource(),
            LocalMarvelDataSource(getDatabase(application).charactersDao)
        )

    private val _loadRepos = MutableLiveData(false)
    private val repoResult: LiveData<Result> = Transformations.map(_loadRepos) {
        repository.refreshCharacters()
    }

    val repos: LiveData<PagedList<Models.Character>> =
        Transformations.switchMap(repoResult) { it -> it.data }
    val networkState: LiveData<NetworkState> = Transformations.switchMap(repoResult) { it ->
        it.networkState
    }

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        _loadRepos.postValue(true)
    }

    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CharactersViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CharactersViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}


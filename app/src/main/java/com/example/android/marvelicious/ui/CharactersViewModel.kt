package com.example.android.marvelicious.ui

import android.app.Application
import androidx.lifecycle.*
import com.example.android.marvelicious.data.Result
import com.example.android.marvelicious.data.source.MarveliciousRepository
import com.example.android.marvelicious.data.source.database.LocalDataSource
import com.example.android.marvelicious.data.source.database.getDatabase
import com.example.android.marvelicious.data.source.network.RemoteDataSource
import com.example.android.marvelicious.domain.Models
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber

class CharactersViewModel(application: Application) : AndroidViewModel(application) {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val repository =
        MarveliciousRepository(
            RemoteDataSource(),
            LocalDataSource(getDatabase(application).charactersDao)
        )

    private val _forceUpdate = MutableLiveData(false)

    private var _characters = Transformations.switchMap(_forceUpdate) {
        if (it) {
            _dataLoading.value = true
            viewModelScope.launch {
                val result = repository.refreshCharacters()
                if (result is Result.Success<*>) {
                } else {
                    Timber.d("result caught in vm")
                    _results.value = result.value
                }
                _dataLoading.value = false
            }
        }
        Transformations.map(repository.observeCharacters()) { result ->
            if (result is Result.Success) {
                result.data
            } else {
                _results.value = repository.observeCharacters().value
                emptyList()
            }
        }
    }

    private val _results = MutableLiveData<Result<List<Models.Character>>>()
    val result: LiveData<Result<List<Models.Character>>>
        get() = _results


    val characters: LiveData<List<Models.Character>>
        get() = _characters

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    init {
        loadCharacters(true)
    }

    fun loadCharacters(forceUpdate: Boolean) {
        _forceUpdate.value = forceUpdate
    }

    fun refresh() {
        _forceUpdate.value = true
    }

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
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


package com.example.android.marvelicious.ui

import android.app.Application
import androidx.lifecycle.*
import com.example.android.marvelicious.database.getDatabase
import com.example.android.marvelicious.domain.Models
import com.example.android.marvelicious.repository.Repository
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.IOException

class CharactersViewModel(application: Application) : AndroidViewModel(application) {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val repository = Repository(getDatabase(application))

    private var _characters = repository.characters
    val characters: LiveData<List<Models.Character>>
        get() = _characters

    private var _eventNetworkError = MutableLiveData(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        refreshDataFromRepository()
    }

    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                repository.refreshCharacters()
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            } catch (networkError: IOException) {
                Timber.e(networkError)
                if (characters.value.isNullOrEmpty()) {
                    _eventNetworkError.value = true
                }
            }
        }
    }


    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
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


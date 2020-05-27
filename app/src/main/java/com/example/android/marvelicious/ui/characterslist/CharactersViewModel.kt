package com.example.android.marvelicious.ui.characterslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.marvelicious.data.source.MarveliciousRepository
import com.example.android.marvelicious.data.source.database.LocalMarvelDataSource
import com.example.android.marvelicious.data.source.database.getDatabase
import com.example.android.marvelicious.data.source.network.RemoteMarvelDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CharactersViewModel(application: Application) : AndroidViewModel(application) {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val repository =
        MarveliciousRepository(
            RemoteMarvelDataSource(),
            LocalMarvelDataSource(getDatabase(application).charactersDao)
        )

    private val characterResult = repository.refreshCharacters()
    val characters = characterResult.data
    val networkState = characterResult.networkState

    fun refresh() {
        viewModelScope.launch {
            characterResult.refresh.invoke()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CharactersViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CharactersViewModel(
                    app
                ) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}


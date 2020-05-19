package com.example.android.marvelicious.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.marvelicious.data.Result
import com.example.android.marvelicious.data.source.database.LocalDataSource
import com.example.android.marvelicious.data.source.network.RemoteDataSource
import com.example.android.marvelicious.domain.Models
import timber.log.Timber


class MarveliciousRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : Repository {
    override suspend fun getCharacters(): Result<List<Models.Character>> {
        return try {
            updateCharactersFromRemoteDataSource()
            localDataSource.getCharacters()
        } catch (ex: Exception) {
            Timber.e(ex)
            Result.Error(ex)
        }
    }

    override fun observeCharacters(): LiveData<Result<List<Models.Character>>> {
        return localDataSource.observeCharacters()
    }

    override suspend fun refreshCharacters(): LiveData<Result<List<Models.Character>>> {
        return updateCharactersFromRemoteDataSource()
    }

    private suspend fun updateCharactersFromRemoteDataSource():
            LiveData<Result<List<Models.Character>>> {

        val networkState = MutableLiveData<Result<List<Models.Character>>>()
        val remoteTasks = remoteDataSource.getCharacters()

        if (remoteTasks is Result.Success) {
            localDataSource.saveCharacters(remoteTasks.data)
            networkState.value = localDataSource.getCharacters()
        } else if (remoteTasks is Result.Error) {
            Timber.e("caught in repo ${remoteTasks.exception}")
            networkState.value = Result.Error(remoteTasks.exception)
        }
        return networkState
    }
}
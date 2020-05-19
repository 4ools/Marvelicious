package com.example.android.marvelicious.data.source

import androidx.paging.LivePagedListBuilder
import com.example.android.marvelicious.data.Result
import com.example.android.marvelicious.data.source.database.LocalMarvelDataSource
import com.example.android.marvelicious.data.source.database.MarveliciousCharacterBoundaryCallback
import com.example.android.marvelicious.data.source.network.RemoteMarvelDataSource
import com.example.android.marvelicious.domain.Models


class MarveliciousRepository(
    private val remoteDataSource: MarvelDataSource,
    private val localDataSource: MarvelDataSource
) : Repository {
    override suspend fun getCharacters(): Result {
        throw NotImplementedError()
    }

    override fun refreshCharacters(): Result {
        return updateCharactersFromRemoteDataSource()
    }

    private fun updateCharactersFromRemoteDataSource():
            Result {
        val dataSourceFactory = localDataSource.getObjectDataSource<Models.Character>()

        val boundaryCallback = MarveliciousCharacterBoundaryCallback(
            remoteDataSource,
            localDataSource
        )

        val data = LivePagedListBuilder(dataSourceFactory, 20)
            .setBoundaryCallback(boundaryCallback)
            .build()

        val networkState = boundaryCallback.networkState

        return Result(data, networkState)
    }
}
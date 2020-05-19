package com.example.android.marvelicious.data.source

import androidx.paging.LivePagedListBuilder
import com.example.android.marvelicious.data.Result
import com.example.android.marvelicious.data.source.database.MarveliciousCharacterBoundaryCallback
import com.example.android.marvelicious.domain.Models


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
        val dataSourceFactory = localDataSource.getObjectDataSource<Models.Character>()

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
}
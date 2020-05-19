package com.example.android.marvelicious.data.source

import androidx.paging.DataSource
import com.example.android.marvelicious.domain.Models

interface MarvelDataSource {
    suspend fun <T> saveObjects(characters: List<T>)
    suspend fun <T> getObjects(): List<T>
    fun <T> getObjectDataSource(): DataSource.Factory<Int, T>
}
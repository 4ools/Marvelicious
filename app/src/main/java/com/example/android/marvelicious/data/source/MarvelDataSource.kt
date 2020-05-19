package com.example.android.marvelicious.data.source

import androidx.paging.DataSource

interface MarvelDataSource {
    suspend fun <T> saveObjects(characters: List<T>)
    suspend fun <T> getObjects(offset: Int, limit: Int): List<T>
    fun <T> getObjectDataSource(): DataSource.Factory<Int, T>
    fun getTotalObjectsCount(): Int
    fun deleteAllObjects()
}
package com.example.android.marvelicious.data.source

import androidx.lifecycle.LiveData
import androidx.paging.DataSource

interface MarvelDataSource {
    suspend fun <T> saveObjects(objectsToSave: List<T>)
    suspend fun <T> getObjects(offset: Int, limit: Int): List<T>
    fun <T> getObjectsDataSource(): DataSource.Factory<Int, T>
    fun getTotalObjectsCount(): Int
    fun deleteAllObjects()
    suspend fun <T> saveObject(objectToSave: T)
    suspend fun <T> getObject(id: Int): T
    fun <T> getObjectDataSource(id: Int): LiveData<T>
}
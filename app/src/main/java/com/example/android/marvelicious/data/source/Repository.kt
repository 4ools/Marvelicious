package com.example.android.marvelicious.data.source

import com.example.android.marvelicious.data.Result

interface Repository {
    suspend fun getCharacters(): Result
    fun refreshCharacters(forceUpdate: Boolean = false):  Result
}
package com.example.android.marvelicious.data.source

import com.example.android.marvelicious.data.Result
import com.example.android.marvelicious.data.SingleResult

interface Repository {
    suspend fun getCharacters(): Result
    fun refreshCharacters(forceUpdate: Boolean = false):  Result
    suspend fun getCharacter(id: Int): SingleResult
    fun refreshCharacter(forceUpdate: Boolean = false, id: Int): SingleResult
}
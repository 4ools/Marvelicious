package com.example.android.marvelicious.data.source

import androidx.lifecycle.LiveData
import com.example.android.marvelicious.data.Result
import com.example.android.marvelicious.domain.Models

interface Repository {
    suspend fun getCharacters(): Result<List<Models.Character>>
    fun observeCharacters(): LiveData<Result<List<Models.Character>>>
    suspend fun refreshCharacters():  LiveData<Result<List<Models.Character>>>
}
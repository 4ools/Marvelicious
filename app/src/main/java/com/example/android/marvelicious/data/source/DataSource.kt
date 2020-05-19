package com.example.android.marvelicious.data.source

import androidx.lifecycle.LiveData
import com.example.android.marvelicious.data.Result
import com.example.android.marvelicious.domain.Models

interface DataSource {
    fun observeCharacters(): LiveData<Result<List<Models.Character>>>

    suspend fun refreshCharacters()

    suspend fun saveCharacters(characters: List<Models.Character>)

    suspend fun getCharacters(): Result<List<Models.Character>>
}
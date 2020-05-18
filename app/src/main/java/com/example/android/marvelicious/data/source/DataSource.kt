package com.example.android.marvelicious.data.source

import androidx.lifecycle.LiveData
import com.example.android.marvelicious.domain.Models

interface DataSource {
    fun observeCharacters(): LiveData<List<Models.Character>>

    suspend fun refreshCharacters()

    suspend fun saveCharacters(characters: List<Models.Character>)

    suspend fun getCharacters(): List<Models.Character>
}
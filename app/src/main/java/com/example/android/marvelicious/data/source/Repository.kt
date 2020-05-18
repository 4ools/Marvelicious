package com.example.android.marvelicious.data.source

import androidx.lifecycle.LiveData
import com.example.android.marvelicious.domain.Models

interface Repository {
    suspend fun getCharacters(): List<Models.Character>
    fun observeCharacters(): LiveData<List<Models.Character>>
    suspend fun refreshCharacters()
}
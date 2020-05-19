package com.example.android.marvelicious.data.source

import com.example.android.marvelicious.domain.Models

interface MarvelDataSource {
    suspend fun saveCharacters(characters: List<Models.Character>)
    suspend fun getCharacters(): List<Models.Character>
}
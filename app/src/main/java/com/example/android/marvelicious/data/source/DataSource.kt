package com.example.android.marvelicious.data.source

import androidx.lifecycle.LiveData
import com.example.android.marvelicious.domain.Models

interface DataSource {
    fun getCharacters(): LiveData<List<Models.Character>>

    fun insertAll(characters: List<Models.Character>)
}
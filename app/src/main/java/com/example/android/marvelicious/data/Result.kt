package com.example.android.marvelicious.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.example.android.marvelicious.domain.Models

data class Result(
    val data: LiveData<PagedList<Models.Character>>,
    val networkState: LiveData<NetworkState>
)

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(val status: Status, val message: String? = null) {
    companion object {
        val LOADED = NetworkState(Status.SUCCESS)
        val LOADING = NetworkState(Status.RUNNING)
        fun error(message: String?) = NetworkState(Status.FAILED, message)
    }
}
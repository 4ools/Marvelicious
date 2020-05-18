package com.example.android.marvelicious.domain

class Models {
    data class Character(
        val id: Int?,
        val name: String?,
        val description: String?,
        val modified: String?,
        val resourceURI: String?,
        val urls: List<Any>?,
        val thumbnail: Image?,
        val comics: Any?,
        val stories: Any?,
        val events: Any?,
        val series: Any?
    )

    data class Image(
        val path: String,
        val extension: String
    )
}
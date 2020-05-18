package com.example.android.marvelicious.domain

import com.example.android.marvelicious.data.source.database.DatabaseCharacter
import com.example.android.marvelicious.data.source.database.DatabaseImage

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

fun List<Models.Character>.asDatabaseModel(): List<DatabaseCharacter>? {
    return this.map {
        DatabaseCharacter(
            id = it.id!!,
            name = it.name,
            description = it.description,
            modified = it.modified,
            resourceURI = it.resourceURI,
            thumbnail = it.thumbnail?.asDatabaseModel()
        )
    }
}

fun Models.Image.asDatabaseModel(): DatabaseImage {
    return DatabaseImage(this.path, this.extension)
}
package com.example.android.marvelicious.data.source.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.marvelicious.domain.Models


@Entity(tableName = "character")
data class DatabaseCharacter constructor(
    @PrimaryKey
    val id: Int,
    val name: String?,
    val description: String?,
    val modified: String?,
    val resourceURI: String?,
    @Embedded val thumbnail: DatabaseImage?
)

data class DatabaseImage(
    val path: String,
    val extension: String
)

fun DatabaseImage.asDomainModel(): Models.Image {
    return Models.Image(this.path, this.extension)
}


fun List<DatabaseCharacter>.asDomainModel(): List<Models.Character> {
    return map {
        Models.Character(
            id = it.id,
            name = it.name,
            description = it.description,
            modified = it.modified,
            resourceURI = it.resourceURI,
            urls = null,
            thumbnail = it.thumbnail?.asDomainModel(),
            comics = null,
            stories = null,
            events = null,
            series = null
        )
    }
}
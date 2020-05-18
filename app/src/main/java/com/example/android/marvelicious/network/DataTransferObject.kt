package com.example.android.marvelicious.network

import com.squareup.moshi.JsonClass

class DataTransferObject {

    @JsonClass(generateAdapter = true)
    data class CharacterDataWrapper(
        val code: Int?, //(int, optional): The HTTP status code of the returned result.,
        val vstatus: String?, //(string, optional): A string description of the call status.,
        val copyright: String?, // (string, optional): The copyright notice for the returned result.,
        val attributionText: String?, // (string, optional): The attribution notice for this result. Please display either this notice or the contents of the attributionHTML field on all screens which contain data from the Marvel Comics API.,
        val attributionHTML: String?, // (string, optional): An HTML representation of the attribution notice for this result. Please display either this notice or the contents of the attributionText field on all screens which contain data from the Marvel Comics API.,
        val data: CharacterDataContainer?, // (CharacterDataContainer, optional): The results returned by the call.
        val etag: String // (string, optional): A digest value of the content returned by the call.
    )

    @JsonClass(generateAdapter = true)
    data class CharacterDataContainer(
        val offset: Int?, // (int, optional): The requested offset (number of skipped results) of the call.,
        val limit: Int?, // (int, optional): The requested result limit.,
        val total: Int?, //  (int, optional): The total number of resources available given the current filter set.,
        val count: Int?, //  (int, optional): The total number of results returned by this call.,
        val results: List<Character> //(Array[Character], optional): The list of characters returned by the call.
    )

    @JsonClass(generateAdapter = true)
    data class Character(
        val id: Int?, // (int, optional): The unique ID of the character resource.,
        val name: String?, // (string, optional): The name of the character.,
        val description: String?, // (string, optional): A short bio or description of the character.,
        val modified: String?, // (Date, optional): The date the resource was most recently modified.,
        val resourceURI: String?, // (string, optional): The canonical URL identifier for this resource.,
        val urls: List<Any>?, // (Array[Url], optional): A set of public web site URLs for the resource.,
        val thumbnail: Image?, // (Image, optional): The representative image for this character.,
        val comics: Any?, // (ComicList, optional): A resource list containing comics which feature this character.,
        val stories: Any?, // (StoryList, optional): A resource list of stories in which this character appears.,
        val events: Any?, // (EventList, optional): A resource list of events in which this character appears.,
        val series: Any? // (SeriesList, optional): A resource list of series in which this character appears.
    )

    @JsonClass(generateAdapter = true)
    data class Image(
        val path: String, // (string, optional): The directory path of to the image.,
        val extension: String // (string, optional): The file extension for the image.
    )


    /**
     * Convert Network results to database objects
     */
//    fun NetworkVideoContainer.asDomainModel(): List<DevByteVideo> {
//        return videos.map {
//            DevByteVideo(
//                title = it.title,
//                description = it.description,
//                url = it.url,
//                updated = it.updated,
//                thumbnail = it.thumbnail
//            )
//        }
//    }
//
//    fun NetworkVideoContainer.asDatabaseModel(): List<DatabaseVideo> {
//        return videos.map {
//            DatabaseVideo(
//                url = it.url,
//                updated = it.updated,
//                title = it.title,
//                description = it.description,
//                thumbnail = it.thumbnail
//            )
//        }
//    }
}
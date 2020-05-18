package com.example.android.marvelicious.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Database(entities = [DatabaseCharacter::class], version = 1)
abstract class MarveliciousDatabase : RoomDatabase() {
    abstract val videoDao: VideoDao
}

@Dao
interface VideoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(characters: List<DatabaseCharacter>)

    @Query("SELECT * FROM databasecharacter ORDER BY name ASC")
    fun getAllCharacters(): LiveData<List<DatabaseCharacter>>
}

private lateinit var INSTANCE: MarveliciousDatabase

fun getDatabase(context: Context): MarveliciousDatabase {
    synchronized(MarveliciousDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                MarveliciousDatabase::class.java,
                "videos"
            ).build()
        }
        return INSTANCE
    }
}
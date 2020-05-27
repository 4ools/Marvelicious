package com.example.android.marvelicious.data.source.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*

@Database(entities = [DatabaseCharacter::class], version = 1)
abstract class MarveliciousDatabase : RoomDatabase() {
    abstract val charactersDao: CharactersDao
}

@Dao
interface CharactersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(characters: List<DatabaseCharacter>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(character: DatabaseCharacter)

    @Query("SELECT * FROM character ORDER BY name ASC")
    fun getAllCharacters(): DataSource.Factory<Int, DatabaseCharacter>

    @Query("SELECT * FROM character ORDER BY name ASC")
    fun getChars(): List<DatabaseCharacter>

    @Query("SELECT COUNT(*) FROM character")
    fun getTotalCharactersCount(): Int

    @Query("DELETE FROM character")
    fun deleteAllCharacters()

    @Query("SELECT * FROM character WHERE id = :characterId ")
    fun getMarvelCharacter(characterId: Int): LiveData<DatabaseCharacter>

    @Query("SELECT * FROM character WHERE id = :characterId ")
    fun gettygetty(characterId: Int): DatabaseCharacter
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
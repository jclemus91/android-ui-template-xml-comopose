package com.jclemus.ui_app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Query("select * from notes order by updatedAt DESC")
    fun getNotes(): Flow<List<Note>>

    @Query("select * from notes where id = :id")
    fun getNoteById(id: Long): Flow<Note?>

    @Query(
        """
            select * from notes
            where title like '%' || :query || '%' or body like '%' || :query || '%'
            order by updatedAt DESC
            """
    )
    fun search(query: String): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(note: Note): Long

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun delete(id: Long)
}
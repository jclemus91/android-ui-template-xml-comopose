package com.jclemus.ui_app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Query("select * from notes order by updatedAt desc")
    fun getAll(): Flow<List<Note>>

    @Query(" select * from notes where id = :id")
    fun getById(id: Long): Flow<Note?>

    @Query(
        """
            select * from notes
            where title like '%' || :query || '%' or body like '%' || :query ||'%' 
            """

    )
    fun search(query: String): Flow<List<Note>>

    @Insert(onConflict = REPLACE)
    suspend fun updateInsert(note: Note): Long

    @Query("delete from notes where id = :id")
    suspend fun delete(id: Long)
}
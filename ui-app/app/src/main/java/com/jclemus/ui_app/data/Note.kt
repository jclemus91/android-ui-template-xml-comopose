package com.jclemus.ui_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.Calendar

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String = "",
    val body: String = "",
    val imageUri: String? = null,
    val createdAt: Long = Calendar.getInstance().time.time,
    val updatedAt: Long = Calendar.getInstance().time.time
)

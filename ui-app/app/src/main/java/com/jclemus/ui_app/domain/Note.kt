package com.jclemus.ui_app.domain

data class Note(
    val id: Long,
    val title: String,
    val body: String,
    val imageUri: String? = null,
    val updatedAt: Long = System.currentTimeMillis()
)

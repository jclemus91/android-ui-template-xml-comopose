package com.jclemus.ui_app.domain


data class Section(
    val id: Long,
    val title: String,
    val homes: List<Home>
)

data class Home(
    val id: Long,
    val imageUrl: String,
    val title: String,
    val price: String,
    val description: String,
    val rating: Float
)


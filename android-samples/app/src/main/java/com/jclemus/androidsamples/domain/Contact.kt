package com.jclemus.androidsamples.domain

data class Contact(
    val id: Long,
    val name: String,
    val email: String,
    val phone: String,
    val company: String = ""
)
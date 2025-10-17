package com.raf.mobiletaskcodeidtest.auth.data.model

data class UserDocument(
    val id: String,
    val email: String,
    val encryptedPassword: String,
    val type: String = "user"
)

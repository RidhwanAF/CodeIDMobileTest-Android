package com.raf.mobiletaskcodeidtest.profile.data.model

data class ProfileDocument(
    val userId: String,
    val name: String,
    val picturePath: String,
    val type: String = "profile"
)

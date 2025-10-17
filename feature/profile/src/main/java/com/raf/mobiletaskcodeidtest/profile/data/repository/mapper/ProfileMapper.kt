package com.raf.mobiletaskcodeidtest.profile.data.repository.mapper

import com.couchbase.lite.MutableDocument
import com.raf.mobiletaskcodeidtest.profile.data.model.ProfileDocument
import com.raf.mobiletaskcodeidtest.profile.domain.model.Profile

object ProfileMapper {
    fun Profile.toProfileDocument() = ProfileDocument(
        userId = userId,
        email = email,
        name = name,
        picturePath = picturePath,
        type = "profile"
    )

    fun MutableDocument.toProfileDocument() = ProfileDocument(
        userId = getString("user_id") ?: id,
        email = getString("email") ?: "",
        name = getString("name") ?: "",
        picturePath = getString("picture_path") ?: ""
    )
}
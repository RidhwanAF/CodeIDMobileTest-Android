package com.raf.mobiletaskcodeidtest.auth.data.repository.mapper

import com.couchbase.lite.MutableDocument
import com.raf.mobiletaskcodeidtest.auth.data.model.UserDocument
import com.raf.mobiletaskcodeidtest.auth.domain.model.User

object UserMapper {
    fun User.toUserDocument(encryptedPassword: String) = UserDocument(
        id = userId,
        email = email,
        encryptedPassword = encryptedPassword
    )

    fun MutableDocument.toUserDocument() = UserDocument(
        id = getString("id") ?: id,
        email = getString("email") ?: "",
        encryptedPassword = getString("encryptedPassword") ?: ""
    )
}
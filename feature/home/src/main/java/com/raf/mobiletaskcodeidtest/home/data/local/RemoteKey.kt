package com.raf.mobiletaskcodeidtest.home.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey
    val label: String,
    val nextOffset: Int?,
)

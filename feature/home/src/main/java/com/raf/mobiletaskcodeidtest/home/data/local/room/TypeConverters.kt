package com.raf.mobiletaskcodeidtest.home.data.local.room

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class TypeConverters {
    @TypeConverter
    fun fromString(value: String): List<String> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun listToString(list: List<String>): String {
        return Json.encodeToString(list)
    }
}
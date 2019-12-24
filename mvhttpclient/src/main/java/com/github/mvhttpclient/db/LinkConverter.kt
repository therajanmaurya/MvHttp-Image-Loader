package com.github.mvhttpclient.db

import androidx.room.TypeConverter
import com.github.mvhttpclient.models.Links
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LinkConverter {

    @TypeConverter
    fun stringToLinks(data: String?): Links? {
        val type = object : TypeToken<Links>() {}.type
        return data?.let { Gson().fromJson(data, type) }
    }

    @TypeConverter
    fun linksToString(links: Links?): String? {
        return links?.let { Gson().toJson(links) }
    }
}

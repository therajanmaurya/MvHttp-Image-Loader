package com.github.mvhttpclient.db

import androidx.room.TypeConverter
import com.github.mvhttpclient.models.Urls
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object UrlConverter {

    @TypeConverter
    @JvmStatic
    fun stringToUrls(data: String?): Urls? {
        val type = object : TypeToken<Urls>() {}.type
        return data?.let { Gson().fromJson(data, type) }
    }

    @TypeConverter
    @JvmStatic
    fun urlsToString(urls: Urls?): String? {
        return urls?.let { Gson().toJson(urls) }
    }
}

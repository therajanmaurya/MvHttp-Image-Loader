package com.github.mvhttpclient.db

import androidx.room.TypeConverter
import com.github.mvhttpclient.models.ProfileImage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ProfileImageConverter {

    @TypeConverter
    @JvmStatic
    fun stringToProfileImage(data: String?): ProfileImage? {
        val type = object : TypeToken<ProfileImage>() {}.type
        return data?.let { Gson().fromJson(data, type) }
    }

    @TypeConverter
    @JvmStatic
    fun profileImageToString(profileImage: ProfileImage?): String? {
        return profileImage?.let { Gson().toJson(profileImage) }
    }
}
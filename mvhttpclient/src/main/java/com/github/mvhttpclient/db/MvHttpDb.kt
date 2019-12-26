package com.github.mvhttpclient.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.mvhttpclient.models.*

@Database(entities = [ImageData::class, Image::class], version = 1, exportSchema = false)
@TypeConverters(LinkConverter::class, UrlConverter::class, ProfileImageConverter::class)
abstract class MvHttpDb : RoomDatabase() {

    abstract fun imageDataDao(): ImageDataDao

    abstract fun imageDao(): ImageDao
}

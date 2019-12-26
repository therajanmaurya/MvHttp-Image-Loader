package com.github.mvhttpclient.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.github.mvhttpclient.models.Image

@Dao
abstract class ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg image: Image)

    @Query("SELECT * FROM image WHERE imageUrl = :url")
    abstract fun getImage(url: String): LiveData<Image>
}
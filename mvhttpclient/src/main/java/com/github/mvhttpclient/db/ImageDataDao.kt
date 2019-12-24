package com.github.mvhttpclient.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.github.mvhttpclient.models.ImageData

@Dao
abstract class ImageDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg image: ImageData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertImages(images: List<ImageData>)

    @Transaction
    @Query("SELECT * FROM imagedata")
    abstract fun getImagesAndUser(): LiveData<List<ImageData>>
}

package com.github.mvhttpclient.repository

import androidx.lifecycle.LiveData
import com.github.mvhttpclient.AppExecutors
import com.github.mvhttpclient.api.MvHttpService
import com.github.mvhttpclient.db.ImageDao
import com.github.mvhttpclient.db.ImageDataDao
import com.github.mvhttpclient.models.Image
import com.github.mvhttpclient.models.ImageData
import com.github.mvhttpclient.utils.ImageUtils
import com.github.mvhttpclient.utils.RateLimiter
import okhttp3.ResponseBody
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class MvHttpRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val mvHttpService: MvHttpService,
    private val imageDataDao: ImageDataDao,
    private val imageDao: ImageDao
) {

    private val repoListRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)

    fun loadImageData(): LiveData<Resource<List<ImageData>>> {
        return object : NetworkBoundResource<List<ImageData>, List<ImageData>>(appExecutors) {
            override fun saveCallResult(item: List<ImageData>) {
                imageDataDao.insertImages(item)
            }

            override fun shouldFetch(data: List<ImageData>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun loadFromDb() = imageDataDao.getImagesAndUser()

            override fun createCall() = mvHttpService.getImageData()

            override fun onFetchFailed() {
                repoListRateLimit.reset("Images")
            }
        }.asLiveData()
    }

    fun loadDbImage(imageUrl: String) = imageDao.getImage(imageUrl)

    fun loadImage(imageUrl: String, height: Int, width: Int, compressPercent: Int): LiveData<Resource<Image>> {
        return object : NetworkBoundResource<Image, ResponseBody>(appExecutors) {
            override fun saveCallResult(item: ResponseBody) {
                imageDao.insert(
                    Image(
                        imageUrl,
                        ImageUtils.compressBitmap(item.source().readByteArray(), compressPercent)
                    )
                )
            }

            override fun shouldFetch(data: Image?): Boolean {
                return data == null || data.data != null
            }

            override fun loadFromDb() = imageDao.getImage(imageUrl)

            override fun createCall() = mvHttpService.getImage(imageUrl, height, width)

            override fun onFetchFailed() {
                repoListRateLimit.reset("Image")
            }
        }.asLiveData()
    }
}

package com.github.mvhttpclient.repository

import androidx.lifecycle.LiveData
import com.github.mvhttpclient.AppExecutors
import com.github.mvhttpclient.api.MvHttpService
import com.github.mvhttpclient.db.ImageDataDao
import com.github.mvhttpclient.models.ImageData
import com.github.mvhttpclient.utils.RateLimiter
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class MvHttpRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val mvHttpService: MvHttpService,
    private val imageDataDao: ImageDataDao
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
}

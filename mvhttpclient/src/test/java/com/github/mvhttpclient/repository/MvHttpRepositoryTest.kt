package com.github.mvhttpclient.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.github.mvhttpclient.api.MvHttpService
import com.github.mvhttpclient.db.ImageDao
import com.github.mvhttpclient.db.ImageDataDao
import com.github.mvhttpclient.db.MvHttpDb
import com.github.mvhttpclient.models.ImageData
import com.github.mvhttpclient.util.ApiUtil.successCall
import com.github.mvhttpclient.util.InstantAppExecutors
import com.github.mvhttpclient.util.TestUtil
import com.github.mvhttpclient.util.mock
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

@RunWith(JUnit4::class)
class MvHttpRepositoryTest {

    private lateinit var repository: MvHttpRepository
    private val dao = Mockito.mock(ImageDataDao::class.java)
    private val imageDao = Mockito.mock(ImageDao::class.java)
    private val service = Mockito.mock(MvHttpService::class.java)

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        val db = Mockito.mock(MvHttpDb::class.java)
        Mockito.`when`(db.imageDataDao()).thenReturn(dao)
        Mockito.`when`(db.imageDao()).thenReturn(imageDao)
        Mockito.`when`(db.runInTransaction(ArgumentMatchers.any())).thenCallRealMethod()
        repository = MvHttpRepository(InstantAppExecutors(), service, dao, imageDao)
    }

    @Test
    fun loadImagesFromNetwork() {
        val dbData = MutableLiveData<List<ImageData>>()
        Mockito.`when`(dao.getImagesAndUser()).thenReturn(dbData)

        val images = TestUtil.getImages
        val call = successCall(images)
        Mockito.`when`(service.getImageData()).thenReturn(call)

        val data = repository.loadImageData()
        Mockito.verify(dao).getImagesAndUser()
        Mockito.verifyNoMoreInteractions(service)

        val observer = mock<Observer<Resource<List<ImageData>>>>()
        data.observeForever(observer)
        Mockito.verifyNoMoreInteractions(service)
        Mockito.verify(observer).onChanged(Resource.loading(null))
        val updatedDbData = MutableLiveData<List<ImageData>>()
        Mockito.`when`(dao.getImagesAndUser()).thenReturn(updatedDbData)

        dbData.postValue(null)
        Mockito.verify(service).getImageData()
        Mockito.verify(dao).insertImages(images)

        updatedDbData.postValue(images)
        Mockito.verify(observer).onChanged(Resource.success(images))
    }
}
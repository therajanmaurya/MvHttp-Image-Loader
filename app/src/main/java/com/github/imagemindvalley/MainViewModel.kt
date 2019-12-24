package com.github.imagemindvalley

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.github.mvhttpclient.models.ImageData
import com.github.mvhttpclient.repository.MvHttpRepository
import com.github.mvhttpclient.repository.Resource
import javax.inject.Inject

open class MainViewModel @Inject constructor(
    private val mvHttpRepository: MvHttpRepository
) : ViewModel() {

    fun getImageData(): LiveData<Resource<List<ImageData>>> =
        Transformations.map(mvHttpRepository.loadImageData()) {
            return@map it
        }
}
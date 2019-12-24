package com.github.mvhttpclient.api

import androidx.lifecycle.LiveData
import com.github.mvhttpclient.models.ImageData
import retrofit2.http.GET

interface MvHttpService {

    @GET("wgkJgazE")
    fun getImageData(): LiveData<ApiResponse<List<ImageData>>>
}
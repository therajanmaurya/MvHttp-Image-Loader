package com.github.mvhttpclient.api

import androidx.lifecycle.LiveData
import com.github.mvhttpclient.models.ImageData
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface MvHttpService {

    @GET("wgkJgazE")
    fun getImageData(): LiveData<ApiResponse<List<ImageData>>>

    @GET
    fun getImage(@Url url: String, @Query("h") height: Int, @Query("w") width: Int): LiveData<ApiResponse<ResponseBody>>
}
package com.github.mvhttpclient.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.mvhttpclient.util.LiveDataTestUtil.getValue
import com.github.mvhttpclient.utils.LiveDataCallAdapterFactory
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class MvHttpServiceTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: MvHttpService

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(MvHttpService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun getImages() {
        enqueueResponse("images.json")
        val images = (getValue(service.getImageData()) as ApiSuccessResponse).body

        val request = mockWebServer.takeRequest()
        assertThat(request.path, `is`("/wgkJgazE"))

        assertThat(images.size, `is`(10))

        val imageData = images[0]
        assertThat(imageData.id, `is`("4kQA1aQK8-Y"))
        assertThat(imageData.color, `is`("#060607"))
        assertThat(imageData.user.username, `is`("nicholaskampouris"))


        val imageData1 = images[1]
        assertThat(imageData1.id, `is`("H_M4dX_F1LQ"))
        assertThat(imageData1.color, `is`("#1C1C18"))
        assertThat(imageData1.user.username, `is`("nicholaskampouris"))
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream("api-response/$fileName")
        val source = Okio.buffer(Okio.source(inputStream))
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(source.readString(Charsets.UTF_8))
        )
    }
}

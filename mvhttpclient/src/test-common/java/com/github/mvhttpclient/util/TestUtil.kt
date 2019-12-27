package com.github.mvhttpclient.util

import com.github.mvhttpclient.models.ImageData
import com.google.gson.reflect.TypeToken

object TestUtil {

    private val testDataFactory: TestDataFactory = TestDataFactory()

    val getImages: List<ImageData>
        get() = testDataFactory.getListTypePojo(
            object : TypeToken<List<ImageData>>() {},
            "images.json"
        )
}

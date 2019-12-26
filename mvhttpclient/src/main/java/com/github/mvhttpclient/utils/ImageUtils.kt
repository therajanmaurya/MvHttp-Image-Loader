package com.github.mvhttpclient.utils

import android.graphics.BitmapFactory
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream


object ImageUtils {

    fun compressBitmap(byteArray: ByteArray?, percent: Int): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size).apply {
            compress(Bitmap.CompressFormat.JPEG, (100 - percent), byteArrayOutputStream)
        }
        return byteArrayOutputStream.toByteArray()
    }
}
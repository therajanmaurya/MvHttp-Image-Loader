package com.github.mvhttpclient.repository

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.github.mvhttpclient.R
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageLoader @Inject constructor(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val mvHttpRepository: MvHttpRepository
) {

    private val imageViewMap = Collections.synchronizedMap(WeakHashMap<ImageView, String>())

    fun loadImage(
        imageView: ImageView,
        imageUrl: String,
        height: Int = 128,
        width: Int = 128,
        compressPercent: Int = 50
    ) {
        imageView.setImageResource(0)
        if (imageViewMap.containsValue(imageUrl)) {
            mvHttpRepository.loadDbImage(imageUrl).observe(lifecycleOwner, Observer {
                if (it?.data != null) {
                    imageView.setImageBitmap(
                        BitmapFactory.decodeByteArray(
                            it.data, 0, it.data.size
                        )
                    )
                }
            })
        } else {
            imageViewMap[imageView] = imageUrl
            mvHttpRepository.loadImage(imageUrl, height, width, compressPercent)
                .observe(lifecycleOwner, Observer { networkImage ->
                    if (networkImage.data != null) {
                        imageView.setImageBitmap(
                            BitmapFactory.decodeByteArray(
                                networkImage.data.data, 0, networkImage.data.data!!.size
                            )
                        )
                    } else {
                        imageView.setImageDrawable(
                            ContextCompat.getDrawable(context, R.drawable.empty_photo)
                        )
                    }
                })
        }
    }

    fun clean() {
        imageViewMap.apply { this.clear() }
    }
}

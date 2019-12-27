package com.github.mvhttpclient.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mvhttpclient.models.Image
import com.github.mvhttpclient.util.LiveDataTestUtil
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ImageDaoTest : DbTest() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun insertImageAndRead() {
        val url =
            "https://images.unsplash.com/photo-1464550838636-1a3496df938b?ixlib=rb-0.3.5&q=80&fm=jpg&crop=entropy&s=ea8f203f18a51214459deec7301f177f"
        val image = Image(url, null)
        db.imageDao().insert(image)
        val loaded = LiveDataTestUtil.getValue(db.imageDao().getImage(url))
        MatcherAssert.assertThat(loaded, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(loaded.imageUrl, CoreMatchers.`is`(url))
        MatcherAssert.assertThat(loaded.data, CoreMatchers.nullValue())
    }
}

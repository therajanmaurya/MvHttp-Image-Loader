package com.github.mvhttpclient.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.mvhttpclient.util.LiveDataTestUtil.getValue
import com.github.mvhttpclient.util.TestUtil
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ImageDataDaoTest : DbTest() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun insertAndRead() {
        val images = TestUtil.getImages
        db.imageDataDao().insertImages(images)
        val loaded = getValue(db.imageDataDao().getImagesAndUser())
        MatcherAssert.assertThat(loaded, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(loaded[0].id, CoreMatchers.`is`("4kQA1aQK8-Y"))
        MatcherAssert.assertThat(loaded[0].width, CoreMatchers.`is`(2448))
        MatcherAssert.assertThat(loaded[0].color, CoreMatchers.`is`("#060607"))
        MatcherAssert.assertThat(loaded[0].likes, CoreMatchers.`is`(12))
    }
}

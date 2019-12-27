package com.github.mvhttpclient.util

import com.github.mvhttpclient.AppExecutors
import java.util.concurrent.Executor

class InstantAppExecutors : AppExecutors(
    instant,
    instant,
    instant
) {
    companion object {
        private val instant = Executor { it.run() }
    }
}

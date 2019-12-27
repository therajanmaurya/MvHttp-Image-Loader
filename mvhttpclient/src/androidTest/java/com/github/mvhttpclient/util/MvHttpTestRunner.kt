package com.github.mvhttpclient.util

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.github.mvhttpclient.TestApp

/**
 * Custom runner to disable dependency injection.
 */
class MvHttpTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, TestApp::class.java.name, context)
    }
}

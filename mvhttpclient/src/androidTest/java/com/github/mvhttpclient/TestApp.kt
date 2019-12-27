package com.github.mvhttpclient

import android.app.Application

/**
 * We use a separate App for tests to prevent initializing dependency injection.
 *
 * See [com.github.mvhttpclient.util.MvHttpTestRunner].
 */
class TestApp : Application()

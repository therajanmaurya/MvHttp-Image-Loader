package com.github.imagemindvalley

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.github.mvhttpclient.repository.ImageLoader
import com.github.mvhttpclient.repository.Status
import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: MainAdapter
    private lateinit var imageLoader: ImageLoader
    private lateinit var sweetUIErrorHandler: SweetUIErrorHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as ImageApplication).dispatchingAndroidInjector.inject(this)
        mainViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        imageLoader = ImageLoader(this, this, mainViewModel.getMvHttpRepository)
        sweetUIErrorHandler = SweetUIErrorHandler(this, findViewById(android.R.id.content))

        adapter = MainAdapter(imageLoader)
        rvImages.setHasFixedSize(true)
        rvImages.adapter = adapter

        findViewById<Button>(R.id.btnTryAgain).setOnClickListener {
            showProgressBar(); fetchImages()
        }

        swipeRefreshLayout.setOnRefreshListener { showProgressBar(); fetchImages() }

        fetchImages()
    }

    private fun fetchImages() {
        mainViewModel.getImageData().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    swipeRefreshLayout.isRefreshing = false
                    if (it.data != null && it.data!!.isNotEmpty()) {
                        sweetUIErrorHandler.hideSweetErrorLayoutUI(
                            swipeRefreshLayout,
                            sweetLayoutError
                        )
                        rvImages.visibility = View.VISIBLE
                        adapter.submitList(it.data)
                        adapter.notifyDataSetChanged()
                    }
                }
                Status.ERROR -> {
                    swipeRefreshLayout.isRefreshing = false
                    sweetUIErrorHandler.showSweetErrorUI(
                        getString(R.string.images), swipeRefreshLayout, sweetLayoutError
                    )
                }
                Status.LOADING -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun showProgressBar() {
        swipeRefreshLayout.isRefreshing = true
        rvImages.visibility = View.GONE
        sweetLayoutError.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        imageLoader.clean()
    }
}

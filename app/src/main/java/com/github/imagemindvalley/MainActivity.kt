package com.github.imagemindvalley

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.github.mvhttpclient.repository.ImageLoader
import com.github.mvhttpclient.repository.Status
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: MainAdapter
    private lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as ImageApplication).dispatchingAndroidInjector.inject(this)
        mainViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        imageLoader = ImageLoader(this,this, mainViewModel.getMvHttpRepository)

        adapter = MainAdapter(imageLoader)
        rvImages.setHasFixedSize(true)
        rvImages.adapter = adapter

        mainViewModel.getImageData().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data != null && it.data!!.isNotEmpty()) {
                        adapter.submitList(it.data)
                        adapter.notifyDataSetChanged()
                    }
                }
                Status.ERROR -> {
                    // Show Error UI
                }
                Status.LOADING -> {
                    // Show Loading Progress Bar
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        imageLoader.clean()
    }
}

package com.github.imagemindvalley

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.mvhttpclient.models.ImageData
import com.github.mvhttpclient.repository.ImageLoader
import kotlinx.android.synthetic.main.item_main.view.*

class MainAdapter(
    private val imageLoader: ImageLoader
) :
    ListAdapter<ImageData, MainAdapter.ViewHolder>(
        ImageDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_main, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageData = getItem(position)
        imageLoader.loadImage(holder.ivNewsIcon, imageData.urls.full, 500, 500, 50)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivNewsIcon = itemView.ivImage!!
    }
}

private class ImageDiffCallback : DiffUtil.ItemCallback<ImageData>() {

    override fun areItemsTheSame(oldItem: ImageData, newItem: ImageData): Boolean {
        return (oldItem.hashCode() == newItem.hashCode())
    }

    override fun areContentsTheSame(oldItem: ImageData, newItem: ImageData): Boolean {
        return (oldItem == newItem)
    }
}

package com.example.newsapp.ui.main.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.newsapp.R

/**
 * This binder class is used to set image in binding layout
 *
 * @param view - input view
 * @param url - this is the image url from article data
 *
 * @author SandeepK
 * @version 1.0
 * */
@BindingAdapter("image")
fun loadImage(view: ImageView, url:String?){
    url?.let {
        Glide.with(view)
            .load(url)
            .placeholder(R.drawable.tools_placeholder)
            .into(view)
    }

}
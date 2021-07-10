package com.example.newsapp.ui.main.adapter

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.newsapp.R

@BindingAdapter("image")
fun loadImage(view: ImageView, url:String?){
    url?.let {
        Glide.with(view)
            .load(url)
            .placeholder(R.drawable.tools_placeholder)
            .into(view)
    }

}
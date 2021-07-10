package com.example.newsapp.ui.main.adapter

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("image")
fun loadImage(view: ImageView, url:String?){
    url?.let {
        Glide.with(view)
            .load(url)
            .into(view)
    }

}
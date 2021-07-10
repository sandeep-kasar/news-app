package com.example.newsapp.data.repository

import com.example.newsapp.data.api.ApiHelper


class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun getTopHeadlines(url:String) = apiHelper.getTopHeadlines(url)
}
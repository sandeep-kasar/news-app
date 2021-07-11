package com.example.newsapp.data.repository

import com.example.newsapp.data.api.ApiHelper

/**
 * This is the main repository class,
 * Here we call the news API
 *
 * @param apiHelper - helper class to call API
 *
 * @author SandeepK
 * @version 1.0
 * */
open class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun getTopHeadlines(url:String) = apiHelper.getTopHeadlines(url)
}
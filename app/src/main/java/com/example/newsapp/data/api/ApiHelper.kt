package com.example.newsapp.data.api

/**
 * This is just a helper class for API call
 *
 * @author SandeepK
 * @version 1.0
 * */
class ApiHelper(private val apiService: ApiService) {

    suspend fun getTopHeadlines(url:String) = apiService.getTopHeadlines(url)
}
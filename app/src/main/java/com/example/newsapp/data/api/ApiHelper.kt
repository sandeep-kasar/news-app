package com.example.newsapp.data.api

class ApiHelper(private val apiService: ApiService) {

    suspend fun getTopHeadlines(url:String) = apiService.getTopHeadlines(url)
}
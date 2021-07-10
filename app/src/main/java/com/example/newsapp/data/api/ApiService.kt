package com.example.newsapp.data.api

import com.example.newsapp.data.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url


interface ApiService {

    @GET
    suspend fun getTopHeadlines(
        @Url url: String,
    ): NewsResponse

}
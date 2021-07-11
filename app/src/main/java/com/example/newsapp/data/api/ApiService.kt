package com.example.newsapp.data.api

import com.example.newsapp.data.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * This interface defines the HTTP methods,
 * Here we have implemented GET method
 *
 * @author SandeepK
 * @version 1.0
 * */

interface ApiService {

    @GET
    suspend fun getTopHeadlines(
        @Url url: String,
    ): NewsResponse

}
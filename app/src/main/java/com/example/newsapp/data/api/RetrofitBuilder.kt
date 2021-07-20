package com.example.newsapp.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * This class creates the retrofit instance
 * It is a singleton class
 *
 * @author SandeepK
 * @version 1.0
 * */
object RetrofitBuilder {

    private const val BASE_URL = "https://newsapi.org/v2/"
    const val SUB_URL_HEAD = "top-headlines?country="
    const val SUB_URL_TAIL = "&category=business&apiKey=f7938edc1ac846e099233b9b1af7d4c1"

    val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)

}
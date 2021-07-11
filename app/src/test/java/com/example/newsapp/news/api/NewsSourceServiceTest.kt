package com.example.newsapp.news.api

import com.example.newsapp.data.api.ApiService
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

@RunWith(JUnit4::class)
class NewsSourceServiceTest : BaseServiceTest() {

    private lateinit var service: ApiService

    @Before
    @Throws(IOException::class)
    fun createService() {
         service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("https://newsapi.org/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Test
    @Throws(IOException::class, InterruptedException::class)
    fun getNewsSource() = runBlocking {
        enqueueResponse("news_source.json")
        val response = service.getTopHeadlines("us")

        // Dummy request
        mockWebServer.takeRequest()

        // Check news source
        assertThat(response, notNullValue())
        assertThat(response.totalResults, `is`(74))
        assertThat(response.status, `is`("ok"))

        // Check list
        val articles = response.articles
        assertThat(articles, notNullValue())

        // Check item 1
        val article1 = articles[0]
        assertThat(article1, notNullValue())
        assertThat(article1.author, `is`("sandeep"))
        assertThat(article1.title, `is`("Google Pixel 2"))
        assertThat(article1.description, `is`("Gift me Google Pixel 2 ;)"))
        assertThat(article1.source.name, `is`("CNN"))
    }
}
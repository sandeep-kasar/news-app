package com.example.newsapp.news.domain

import com.example.newsapp.data.api.ApiHelper
import com.example.newsapp.data.api.ApiService
import com.example.newsapp.data.api.RetrofitBuilder
import com.example.newsapp.data.model.NewsArticle
import com.example.newsapp.data.model.NewsResponse
import com.example.newsapp.data.repository.MainRepository
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock

/**
 * Test the repository
 * Use mockito
 *
 * @author SandeepK
 * @version 1.0
 * */
@RunWith(JUnit4::class)
class NewsRepositoryTest : MockitoTest() {

    @Mock
    lateinit var newsSourceService: ApiService

    @Mock
    lateinit var newsRepository: MainRepository

    @Before
    fun init(){
        newsRepository = MainRepository(ApiHelper(newsSourceService))
    }

    @Test
    fun `get news articles from web`() = runBlocking {

        // GIVEN
        val apiUrl = RetrofitBuilder.SUB_URL_HEAD + "us" + RetrofitBuilder.SUB_URL_TAIL
        val fetchedArticles = listOf(
            NewsArticle(title = "Fetched 1", source = NewsArticle.Source()),
            NewsArticle(title = "Fetched 2", source = NewsArticle.Source())
        )
        val newsResponse = NewsResponse(articles = fetchedArticles)


        // WHEN
        whenever(newsSourceService.getTopHeadlines(apiUrl)) doReturn newsResponse

        // THEN
        assertThat(newsResponse.articles, CoreMatchers.notNullValue())
    }
}